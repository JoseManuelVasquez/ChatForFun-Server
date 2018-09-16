package com.jmvc.chatforfun.protocol;

import static org.junit.jupiter.api.Assertions.*;

import static com.jmvc.chatforfun.protocol.Command.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.junit.jupiter.api.Test;

/**
 * @author JMVC
 *
 */
class ProtocolTest {
	
	private static final String USERNAME = "test";
	private static final String PASSWORD = "test";
	private static final String DISALLOWED_COMMAND = "STRX";
	private static final int PORT = 9999;
	private static final String HOST = "localhost";
	
	private Socket socket;
	private ServerSocket serverSocket;
	
	@Test
	void readCommand()
	{
		/* Server */
		Thread thread1 = new Thread(new Runnable()
		{

			@Override
			public void run()
			{
				try
				{
					serverSocket = new ServerSocket(PORT);
					socket = serverSocket.accept();
					System.out.println("ACCEPTED\n");
					Protocol protocol = new Protocol(socket);
					while(protocol.readValidCommand());
				}
				catch (IOException e)
				{
					System.out.println(e.getMessage());
				}
			}
	
		});
		thread1.start();
		
		/* Client */
		Thread thread2 = new Thread(new Runnable()
		{

			@Override
			public void run()
			{
				try {
					/* Test Connection */
					Socket clientSocket = new Socket(HOST, PORT);
					
					DataInputStream dis = new DataInputStream(clientSocket.getInputStream());
					DataOutputStream dos = new DataOutputStream(clientSocket.getOutputStream());
					
					/* REGISTER COMMAND */
					
					ProtocolUtils.write_command32(dos, REGISTER);
					ProtocolUtils.write_char(dos, ' ');
					ProtocolUtils.write_command32(dos, START_TAG);
					ProtocolUtils.write_int32(dos, USERNAME.length());
					ProtocolUtils.write_command_variable(dos, USERNAME);
					ProtocolUtils.write_command32(dos, END_TAG);
					ProtocolUtils.write_command32(dos, START_TAG);
					ProtocolUtils.write_int32(dos, PASSWORD.length());
					ProtocolUtils.write_command_variable(dos, PASSWORD);
					ProtocolUtils.write_command32(dos, END_TAG);
					
					/* LOGIN COMMAND */
					
					ProtocolUtils.write_command32(dos, LOGIN);
					ProtocolUtils.write_char(dos, ' ');
					ProtocolUtils.write_command32(dos, START_TAG);
					ProtocolUtils.write_int32(dos, USERNAME.length());
					ProtocolUtils.write_command_variable(dos, USERNAME);
					ProtocolUtils.write_command32(dos, END_TAG);
					ProtocolUtils.write_command32(dos, START_TAG);
					ProtocolUtils.write_int32(dos, PASSWORD.length());
					ProtocolUtils.write_command_variable(dos, PASSWORD);
					ProtocolUtils.write_command32(dos, END_TAG);
					
					String command = ProtocolUtils.read_command32(dis);
					System.out.println("Server Response: " + command);
					assertEquals(LOGGED_IN, command);
					
					ProtocolUtils.write_command32(dos, DELETE_ACCOUNT);
					
					command = ProtocolUtils.read_command32(dis);
					System.out.println("Server Response: " + command +
									   ProtocolUtils.read_char(dis) +
									   ProtocolUtils.read_int32(dis));
					assertEquals(ERROR, command);
					
					/* DISALLOWED COMMAND */
					
					ProtocolUtils.write_command32(dos, DISALLOWED_COMMAND);
					ProtocolUtils.write_char(dos, ' ');
					ProtocolUtils.write_command32(dos, START_TAG);
					ProtocolUtils.write_int32(dos, USERNAME.length());
					ProtocolUtils.write_command_variable(dos, USERNAME);
					ProtocolUtils.write_command32(dos, END_TAG);
					ProtocolUtils.write_command32(dos, START_TAG);
					ProtocolUtils.write_int32(dos, PASSWORD.length());
					ProtocolUtils.write_command_variable(dos, PASSWORD);
					ProtocolUtils.write_command32(dos, END_TAG);
					
					command = ProtocolUtils.read_command32(dis);
					System.out.println("Server Response: " + command +
									   ProtocolUtils.read_char(dis) +
									   ProtocolUtils.read_int32(dis));
					assertEquals(ERROR, command);
					
					/* We don't need our test file and streams anymore */
			        clientSocket.close();
				}
				catch (IOException e)
				{
					System.out.println(e.getMessage());
				}
			}
	
		});
		thread2.start();
		try
		{
			thread2.join();
		}
		catch (InterruptedException e)
		{
			System.out.println(e.getMessage());
		}
	}

}
