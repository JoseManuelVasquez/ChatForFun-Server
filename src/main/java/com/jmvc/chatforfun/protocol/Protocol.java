package com.jmvc.chatforfun.protocol;

import static com.jmvc.chatforfun.protocol.Error.*;

import static com.jmvc.chatforfun.protocol.Command.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.jmvc.chatforfun.server.AccessDB;

/**
 * @author JMVC
 *
 */
public class Protocol implements IProtocol{
	
	private DataOutputStream out;
	private DataInputStream in;
	private static List<UserSocketData> usersOnline;
	private UserSocketData currentUser;
	private Socket userSocket;
	
	/**
	 * Constructor for each client and his connection
	 * @param socket
	 * @throws IOException
	 */
	public Protocol(Socket socket) throws IOException
	{
		out = new DataOutputStream(socket.getOutputStream());
		in = new DataInputStream(socket.getInputStream());
		if(usersOnline != null)
			usersOnline = new ArrayList<>();
		userSocket = socket;
	}
	
	/* --------------------------------------- PROTOCOL READING METHODS --------------------------------------- */

	/**
	 * Reading only a 32-bit valid command
	 */
	@Override
	public boolean readValidCommand()
	{
		try
		{
			String command = ProtocolUtils.read_command32(in);
			command = command.toUpperCase();
			
			if(command.length() == 4 && Arrays.asList(VALID_COMMANDS).contains(command))
			{
				/* First, we read a space character */
				if(ProtocolUtils.read_char(in) != ' ')
					return false;
				
				boolean resultCommand = false;
				
				switch(command)
				{
					case REGISTER:
						resultCommand = readRGSTCommand();
						if(!resultCommand)
							writeERROCommand(ERROR_USER_EXISTS);
						break;
					case LOGIN:
						resultCommand = readLOINCommand();
						if(!resultCommand)
							writeERROCommand(ERROR_USER_OR_PASSWORD_WRONG);
						break;
					case LOGOUT:
						readLOUTCommand();
						break;
					case SEND_MESSAGE:
						resultCommand = readSENDCommand();
						if(!resultCommand)
							writeERROCommand(ERROR_WHILE_TRYING_TO_SEND_MSG);
						break;
					case ADD_FRIEND:
						resultCommand = readADDFCommand();
						if(!resultCommand)
							writeERROCommand(ERROR_WHILE_TRYING_TO_ADD);
						break;
					case DELETE_FRIEND:
						resultCommand = readDELFCommand();
						if(!resultCommand)
							writeERROCommand(ERROR_WHILE_TRYING_TO_DEL);
						break;
					case DELETE_ACCOUNT:
						resultCommand = readDELACommand();
						if(!resultCommand)
							writeERROCommand(ERROR_UNKNOWN);
						break;
					case EXIT:
						return false;
				}
				
				return resultCommand;
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * RGST command sent from client, REGISTER
	 * @return boolean
	 * @throws IOException
	 */
	private boolean readRGSTCommand() throws IOException
	{
		List<String> params = readCommandTwoParameters();
		
		if(params == null)
			return false;
		
		String userName = params.get(0);
		String userPass = params.get(1);
		
		return AccessDB.registerUser(userName, userPass);
	}

	/**
	 * LOIN command sent from client, LOGIN
	 * @return boolean
	 * @throws IOException
	 */
	private boolean readLOINCommand() throws IOException
	{
		List<String> params = readCommandTwoParameters();
		
		if(params == null)
			return false;
		
		String userName = params.get(0);
		String userPass = params.get(1);
		
		boolean isLogged = AccessDB.authenticateUser(userName, userPass);
		if(isLogged)
		{
			currentUser = new UserSocketData(userName, userPass);
			currentUser.setUserLogged(true);
			currentUser.setSocket(userSocket);
			usersOnline.add(currentUser);
			writeLGGDCommand();
		}
		
		return isLogged;
	}

	/**
	 * LOUT command sent from client, LOGOUT
	 * @return boolean
	 * @throws IOException
	 */
	private boolean readLOUTCommand()
	{
		if(currentUser == null || !currentUser.isUserLogged())
			return false;

		usersOnline.remove(currentUser);
		currentUser = null;
		
		return true;
	}

	/**
	 * SEND command sent from client, SEND A MESSAGE TO FRIEND
	 * @return boolean
	 * @throws IOException
	 */
	private boolean readSENDCommand() throws IOException
	{
		if(currentUser == null || !currentUser.isUserLogged())
			return false;
		
		List<String> params = readCommandTwoParameters();
		
		if(params == null)
			return false;
		
		String friend = params.get(0);
		String message = params.get(1);
		
		List<String> friends = AccessDB.getFriendsOf(currentUser.getUser(), currentUser.getPassword());
		Iterator<String> iter = friends.iterator();
		boolean friendFound = false;
		while(iter.hasNext() && !friendFound)
			friendFound = friend.equals(iter.next());
		
		if(friendFound)
		{
			Iterator<UserSocketData> iterSocketFriend = usersOnline.iterator();
			UserSocketData friendSocketData = null;
			while(iterSocketFriend.hasNext() && !friendFound)
			{
				friendSocketData = iterSocketFriend.next();
				friendFound = friend.equals(friendSocketData.getUser());
			}
			
			if(friendFound)
			{
				DataOutputStream auxOut = out;
				out = new DataOutputStream(friendSocketData.getSocket().getOutputStream());
				writeRVEDCommand(friend, message);
				out = auxOut;
				writeSDEDCommand(friend);
			}
		}
		
		return friendFound;
	}

	/**
	 * ADDF command sent from client, ADD A FRIEND
	 * @return boolean
	 * @throws IOException
	 */
	private boolean readADDFCommand() throws IOException
	{
		if(currentUser == null || !currentUser.isUserLogged())
			return false;
		
		String friend = readCommandOneParameter();
		
		if(friend == null)
			return false;
		
		if(!AccessDB.addFriend(currentUser.getUser(), currentUser.getPassword(), friend))
			return false;
			
		writeADEDCommand(friend);
		
		return true;
	}

	/**
	 * DELF command sent from client, DELETE A FRIEND
	 * @return boolean
	 * @throws IOException
	 */
	private boolean readDELFCommand() throws IOException
	{
		if(currentUser == null || !currentUser.isUserLogged())
			return false;
		
		String friend = readCommandOneParameter();
		
		if(friend == null)
			return false;
		
		writeDELDCommand(friend);
		
		return true;
	}
	
	/**
	 * DELA command sent from client, DELETE ACOUNT
	 * @return boolean
	 * @throws IOException
	 */
	private boolean readDELACommand() throws IOException
	{
		if(currentUser == null || !currentUser.isUserLogged())
			return false;
		
		boolean isDeleted = AccessDB.deleteUser(currentUser.getUser(), currentUser.getPassword());
		if(isDeleted)
			writeERROCommand(ERROR_EXPIRED_SESSION);
		
		return isDeleted;
	}
	
	/**
	 * Skeleton of command with 1 parameter
	 * @return String
	 * @throws IOException 
	 */
	private String readCommandOneParameter() throws IOException
	{
		/* We read a 32-bit tag parameter <START_PARAMETER> */
		if(!ProtocolUtils.read_command32(in).equals(Command.START_TAG))
			return null;
		
		String param = null;
		
		int numberChars = ProtocolUtils.read_int32(in);
		param = ProtocolUtils.read_command_variable(in, numberChars);
		
		if(!ProtocolUtils.read_command32(in).equals(Command.END_TAG))
			return null;
		
		return param;
		
	}
	
	/**
	 * Skeleton of command with 2 parameters
	 * @return List
	 * @throws IOException 
	 */
	private List<String> readCommandTwoParameters() throws IOException
	{
		/* We read a 32-bit tag parameter <START_PARAMETER> */
		if(!ProtocolUtils.read_command32(in).equals(Command.START_TAG))
			return null;
			
		List<String> params = new ArrayList<>();
		
		int numberChars = ProtocolUtils.read_int32(in);
		params.add(ProtocolUtils.read_command_variable(in, numberChars));
		
		if(!ProtocolUtils.read_command32(in).equals(Command.END_TAG) ||
		   !ProtocolUtils.read_command32(in).equals(Command.START_TAG))
			return null;
		
		numberChars = ProtocolUtils.read_int32(in);
		params.add(ProtocolUtils.read_command_variable(in, numberChars));
		
		if(!ProtocolUtils.read_command32(in).equals(Command.END_TAG))
			return null;
		
		if(params.size() != 2)
			params = null;
		
		return params;
		
	}

	
	/* --------------------------------------- PROTOCOL WRITING METHODS --------------------------------------- */
	
	/**
	 * LGGD command sent to client, LOGGED IN
	 */
	@Override
	public void writeLGGDCommand()
	{
		try
		{
			ProtocolUtils.write_command32(out, LOGGED_IN);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * SDED command sent to client, MESSAGE SENT
	 */
	@Override
	public void writeSDEDCommand(String friend)
	{
		sendCommandOneParameter(MESSAGE_SENT, friend);
	}

	/**
	 * RVED command sent to client, MESSAGE RECEIVED
	 */
	@Override
	public void writeRVEDCommand(String friend, String message)
	{
		sendCommandTwoParameters(MESSAGE_RECEIVED, friend, message);
	}

	/**
	 * ADED command sent to client, FRIEND HAS BEEN ADDED
	 */
	@Override
	public void writeADEDCommand(String friend)
	{
		sendCommandOneParameter(FRIEND_ADDED, friend);
	}

	/**
	 * DELD command sent to client, FRIEND HAS BEEN DELETED
	 */
	@Override
	public void writeDELDCommand(String friend)
	{
		sendCommandOneParameter(FRIEND_DELETED, friend);
	}

	/**
	 * FDIN command sent to client, FRIEND HAS LOGGED IN
	 */
	@Override
	public void writeFDINCommand(String friend)
	{
		sendCommandOneParameter(FRIEND_LOGGED_IN, friend);
	}

	/**
	 * FOUT command sent to client, FRIEND HAS LOGGED OUT
	 */
	@Override
	public void writeFOUTCommand(String friend)
	{
		sendCommandOneParameter(FRIEND_LOGGED_OUT, friend);
	}

	/**
	 * ERRO command sent to client, ERROR
	 */
	@Override
	public void writeERROCommand(int errorCode)
	{
		try
		{
			ProtocolUtils.write_command32(out, ERROR);
			ProtocolUtils.write_char(out, ' ');
			ProtocolUtils.write_int32(out, errorCode);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Skeleton of command with 1 parameter
	 * @param command
	 * @param friend
	 */
	private void sendCommandOneParameter(String command, String param)
	{
		try
		{
			ProtocolUtils.write_command32(out, command);
			ProtocolUtils.write_char(out, ' ');
			ProtocolUtils.write_command32(out, START_TAG);
			ProtocolUtils.write_int32(out, param.length());
			ProtocolUtils.write_command_variable(out, param);
			ProtocolUtils.write_command32(out, END_TAG);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Skeleton of command with 2 parameters
	 * @param command
	 * @param friend
	 */
	private void sendCommandTwoParameters(String command, String param1, String param2)
	{
		try
		{
			ProtocolUtils.write_command32(out, command);
			ProtocolUtils.write_char(out, ' ');
			ProtocolUtils.write_command32(out, START_TAG);
			ProtocolUtils.write_int32(out, param1.length());
			ProtocolUtils.write_command_variable(out, param1);
			ProtocolUtils.write_command32(out, END_TAG);
			ProtocolUtils.write_command32(out, START_TAG);
			ProtocolUtils.write_int32(out, param2.length());
			ProtocolUtils.write_command_variable(out, param2);
			ProtocolUtils.write_command32(out, END_TAG);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

}
