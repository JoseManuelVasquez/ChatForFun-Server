package com.jmvc.chatforfun.server;

import java.util.ArrayList;
import java.util.List;

import com.jmvc.chatforfun.model.DAOPendingMessage;
import com.jmvc.chatforfun.model.DAOUser;
import com.jmvc.chatforfun.model.DTOPendingMessage;
import com.jmvc.chatforfun.model.DTOUser;
import com.jmvc.chatforfun.protocol.Command;

/**
 * @author JMVC
 *
 */
public final class AccessDB {
	
	private AccessDB() {}
	
	/**
	 * Register a new user
	 * @param user
	 * @param password
	 * @return boolean
	 */
	public static boolean registerUser(String user, String password)
	{
		DAOUser daoUser = new DAOUser();
		DTOUser dtoUser = new DTOUser(user, password);
		if(daoUser.existsUser(user))
			return false;
		
		daoUser.createUser(dtoUser);
		
		return true;
	}
	
	/**
	 * Authentication given an user and password
	 * @param user
	 * @param password
	 * @return boolean
	 */
	public static boolean authenticateUser(String user, String password)
	{
		DAOUser daoUser = new DAOUser();
		DTOUser dtoUser = new DTOUser(user, password);
		if(!daoUser.existsUser(user))
			return false;

		return true;
	}
	
	/**
	 * Delete a specific user
	 * @param user
	 * @param password
	 * @return boolean
	 */
	public static boolean deleteUser(String user, String password)
	{
		DAOUser daoUser = new DAOUser();
		DTOUser dtoUser = new DTOUser(user, password);
		if(!daoUser.existsUser(user))
			return false;
		
		daoUser.deleteUser(dtoUser);
		
		return true;
	}
	
	/**
	 * Delete a specific user
	 * @param user
	 * @param password
	 * @param friend
	 * @return boolean
	 */
	public static boolean addFriend(String user, String password, String friend)
	{
		DAOUser daoUser = new DAOUser();
		DTOUser dtoUser = new DTOUser(user, password);
		if(!daoUser.existsUser(friend))
			return false;

		daoUser.createFriend(dtoUser, friend);
		
		return true;
	}
	
	/**
	 * Delete a specific friend
	 * @param user
	 * @param password
	 * @param friend
	 * @return boolean
	 */
	public static boolean deleteFriend(String user, String password, String friend)
	{
		DAOUser daoUser = new DAOUser();
		DTOUser dtoUser = new DTOUser(user, password);
		if(!daoUser.existsUser(friend) || !daoUser.isFriendOf(dtoUser, friend))
			return false;
		
		daoUser.deleteFriend(dtoUser, friend);
		
		return true;
	}

	/**
	 * Get friends of a specific user
	 * @param user
	 * @param password
	 * @return List
	 */
	public static List<String> getFriendsOf(String user, String password)
	{
		DAOUser daoUser = new DAOUser();
		DTOUser dtoUser = new DTOUser(user, password);
		if(!daoUser.existsUser(user))
			return null;
		
		List<String> friends = daoUser.selectFriends(dtoUser);
		if(friends.isEmpty())
			return null;
		
		return friends;
	}

	/**
	 * Add a pending message
	 * @param user
	 * @param message
	 * @return boolean
	 */
	public static boolean addPendingMessage(String user, String friend, String message)
	{
		DAOPendingMessage daoPMessage = new DAOPendingMessage();
		DTOPendingMessage dtoPMessage = new DTOPendingMessage(user, friend, message);
		daoPMessage.createMessage(dtoPMessage);

		return true;
	}

	/**
	 * Return a list of messages that user has not read yet
	 * @param user
	 * @return List
	 */
	public static List<String> getPendingMessages(String user)
	{
		DAOPendingMessage daoPMessage = new DAOPendingMessage();
		List<String> messages = daoPMessage.selectAllMessages(user);
		List<String> fromFriend = daoPMessage.selectAllFriends(user);

		/* We concatenate a list of messages + friends */
		List<String> messagesFromFriend = new ArrayList<>();
		String msg;
		for(int i=0; i < messages.size(); i++)
		{
			msg = "";
			msg = msg.concat(messages.get(i));
			msg = msg.concat(Command.START_TAG);
			msg = msg.concat(fromFriend.get(i));
			messagesFromFriend.add(msg);
		}

		daoPMessage.deleteAllMessages(user);

		return messagesFromFriend;
	}
}
