package com.jmvc.chatforfun.protocol;

import static org.junit.jupiter.api.Assertions.*;

import static com.jmvc.chatforfun.protocol.Command.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.jmvc.chatforfun.model.Database;
import com.jmvc.chatforfun.server.AccessDB;
import org.junit.jupiter.api.Test;

/**
 * @author JMVC
 *
 */
class ProtocolTest {
	
	private static final String USERNAME = "test";
	private static final String PASSWORD = "test";
    private static final String USERNAME2 = "testFriend";
	private static final String PASSWORD2 = "testFriend";
	private static final String USERNAME3 = "testFriend2";
	private static final String PASSWORD3 = "testFriend2";
	private static final String DISALLOWED_COMMAND = "STRX";
	private static final String MESSAGE = "This is a test message";
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
					System.out.println("CONNECTED\n");
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
					ProtocolUtils.write_int32(dos, USERNAME.length());
					ProtocolUtils.write_command_variable(dos, USERNAME);
					ProtocolUtils.write_int32(dos, PASSWORD.length());
					ProtocolUtils.write_command_variable(dos, PASSWORD);

					String command = ProtocolUtils.read_command32(dis);
					System.out.println("Server Response: " + command);
					assertEquals(REGISTERED, command);
					
					/* LOGIN COMMAND */
					
					ProtocolUtils.write_command32(dos, LOGIN);
					ProtocolUtils.write_char(dos, ' ');
					ProtocolUtils.write_int32(dos, USERNAME.length());
					ProtocolUtils.write_command_variable(dos, USERNAME);
					ProtocolUtils.write_int32(dos, PASSWORD.length());
					ProtocolUtils.write_command_variable(dos, PASSWORD);

					command = ProtocolUtils.read_command32(dis);
					System.out.println("Server Response: " + command);
					assertEquals(LOGGED_IN, command);

                    command = ProtocolUtils.read_command32(dis);
                    System.out.println("Server Response: " + command);

                    assertEquals(NO_FRIENDS, command);

                    /* ADD A NEW USER, LOGIN AND ADD FRIEND */

                    /* REGISTER */
                    ProtocolUtils.write_command32(dos, REGISTER);
                    ProtocolUtils.write_char(dos, ' ');
                    ProtocolUtils.write_int32(dos, USERNAME2.length());
                    ProtocolUtils.write_command_variable(dos, USERNAME2);
                    ProtocolUtils.write_int32(dos, PASSWORD2.length());
                    ProtocolUtils.write_command_variable(dos, PASSWORD2);

					command = ProtocolUtils.read_command32(dis);
					System.out.println("Server Response: " + command);
					assertEquals(REGISTERED, command);

                    /* LOGIN  */
                    ProtocolUtils.write_command32(dos, LOGIN);
                    ProtocolUtils.write_char(dos, ' ');
                    ProtocolUtils.write_int32(dos, USERNAME2.length());
                    ProtocolUtils.write_command_variable(dos, USERNAME2);
                    ProtocolUtils.write_int32(dos, PASSWORD2.length());
                    ProtocolUtils.write_command_variable(dos, PASSWORD2);

                    command = ProtocolUtils.read_command32(dis);
                    System.out.println("Server Response: " + command);

                    command = ProtocolUtils.read_command32(dis);
                    System.out.println("Server Response: " + command);
                    assertEquals(NO_FRIENDS, command);

                    /* ADD FRIEND */
                    ProtocolUtils.write_command32(dos, ADD_FRIEND);
                    ProtocolUtils.write_char(dos, ' ');
                    ProtocolUtils.write_int32(dos, USERNAME.length());
                    ProtocolUtils.write_command_variable(dos, USERNAME);

                    command = ProtocolUtils.read_command32(dis);
                    System.out.print("Server Response: " + command + ProtocolUtils.read_char(dis));
                    assertEquals(FRIEND_ADDED, command);

                    int len = ProtocolUtils.read_int32(dis);
                    System.out.println(ProtocolUtils.read_command_variable(dis, len));

                    /* LOGIN */
                    ProtocolUtils.write_command32(dos, LOGIN);
                    ProtocolUtils.write_char(dos, ' ');
                    ProtocolUtils.write_int32(dos, USERNAME2.length());
                    ProtocolUtils.write_command_variable(dos, USERNAME2);
                    ProtocolUtils.write_int32(dos, PASSWORD2.length());
                    ProtocolUtils.write_command_variable(dos, PASSWORD2);

                    command = ProtocolUtils.read_command32(dis);
                    System.out.println("Server Response: " + command);
                    assertEquals(LOGGED_IN, command);

                    /* LIST OF FRIENDS */
                    command = ProtocolUtils.read_command32(dis);
                    System.out.print("Server Response: " + command + ProtocolUtils.read_char(dis));
                    int lenUsers = ProtocolUtils.read_int32(dis);
                    for(int i = 0; i < lenUsers; i++)
                    {
                        len = ProtocolUtils.read_int32(dis);
                        System.out.print(ProtocolUtils.read_command_variable(dis, len) + " ");
                    }
                    System.out.println();

					command = ProtocolUtils.read_command32(dis);
					System.out.print("Server Response: " + command +
							ProtocolUtils.read_char(dis));
					len = ProtocolUtils.read_int32(dis);
					System.out.print(ProtocolUtils.read_command_variable(dis, len) + "\n");

                    /* SEND A MESSAGE TO FRIEND */

					ProtocolUtils.write_command32(dos, SEND_MESSAGE);
					ProtocolUtils.write_char(dos, ' ');
					ProtocolUtils.write_int32(dos, USERNAME.length());
					ProtocolUtils.write_command_variable(dos, USERNAME);
					ProtocolUtils.write_int32(dos, MESSAGE.length());
					ProtocolUtils.write_command_variable(dos, MESSAGE);

					command = ProtocolUtils.read_command32(dis);
					System.out.print("Server Response: " + command +
							ProtocolUtils.read_char(dis));
					len = ProtocolUtils.read_int32(dis);
					System.out.print(ProtocolUtils.read_command_variable(dis, len) + "\n");

					/* RECEIVED MESSAGE */
					command = ProtocolUtils.read_command32(dis);
					System.out.print("Server Response: " + command +
							ProtocolUtils.read_char(dis));
					len = ProtocolUtils.read_int32(dis);
					System.out.print(ProtocolUtils.read_command_variable(dis, len) + " ");
					len = ProtocolUtils.read_int32(dis);
					System.out.print(ProtocolUtils.read_command_variable(dis, len) + "\n");

					/* TEST THE BUFFER BEHIND A OFFLINE USER */

					/* REGISTER NEW FRIEND */
					ProtocolUtils.write_command32(dos, REGISTER);
					ProtocolUtils.write_char(dos, ' ');
					ProtocolUtils.write_int32(dos, USERNAME3.length());
					ProtocolUtils.write_command_variable(dos, USERNAME3);
					ProtocolUtils.write_int32(dos, PASSWORD3.length());
					ProtocolUtils.write_command_variable(dos, PASSWORD3);

					command = ProtocolUtils.read_command32(dis);
					System.out.println("Server Response: " + command);
					assertEquals(REGISTERED, command);

					/* ADD FRIEND */
					ProtocolUtils.write_command32(dos, ADD_FRIEND);
					ProtocolUtils.write_char(dos, ' ');
					ProtocolUtils.write_int32(dos, USERNAME3.length());
					ProtocolUtils.write_command_variable(dos, USERNAME3);

					command = ProtocolUtils.read_command32(dis);
					System.out.print("Server Response: " + command + ProtocolUtils.read_char(dis));
					assertEquals(FRIEND_ADDED, command);
					len = ProtocolUtils.read_int32(dis);
					System.out.println(ProtocolUtils.read_command_variable(dis, len));

					/* SEND A MESSAGE TO FRIEND OFFLINE USER3 FROM USER2 */
					ProtocolUtils.write_command32(dos, SEND_MESSAGE);
					ProtocolUtils.write_char(dos, ' ');
					ProtocolUtils.write_int32(dos, USERNAME3.length());
					ProtocolUtils.write_command_variable(dos, USERNAME3);
					ProtocolUtils.write_int32(dos, MESSAGE.length());
					ProtocolUtils.write_command_variable(dos, MESSAGE);

					command = ProtocolUtils.read_command32(dis);
					System.out.print("Server Response: " + command +
							ProtocolUtils.read_char(dis));
					len = ProtocolUtils.read_int32(dis);
					System.out.print(ProtocolUtils.read_command_variable(dis, len) + "\n");

					/* LOGIN */
					ProtocolUtils.write_command32(dos, LOGIN);
					ProtocolUtils.write_char(dos, ' ');
					ProtocolUtils.write_int32(dos, USERNAME3.length());
					ProtocolUtils.write_command_variable(dos, USERNAME3);
					ProtocolUtils.write_int32(dos, PASSWORD3.length());
					ProtocolUtils.write_command_variable(dos, PASSWORD3);

					command = ProtocolUtils.read_command32(dis);
					System.out.println("Server Response: " + command);
					assertEquals(LOGGED_IN, command);

					/* LIST OF FRIENDS */
					command = ProtocolUtils.read_command32(dis);
					System.out.print("Server Response: " + command + ProtocolUtils.read_char(dis));
					lenUsers = ProtocolUtils.read_int32(dis);
					for(int i = 0; i < lenUsers; i++)
					{
						len = ProtocolUtils.read_int32(dis);
						System.out.print(ProtocolUtils.read_command_variable(dis, len) + " ");
					}
					System.out.println();

					/* RECEIVED MESSAGE */
					command = ProtocolUtils.read_command32(dis);
					System.out.print("Server Response: " + command +
							ProtocolUtils.read_char(dis));
					len = ProtocolUtils.read_int32(dis);
					System.out.print(ProtocolUtils.read_command_variable(dis, len) + " ");
					len = ProtocolUtils.read_int32(dis);
					System.out.print(ProtocolUtils.read_command_variable(dis, len) + "\n");

					command = ProtocolUtils.read_command32(dis);
					System.out.print("Server Response: " + command +
							ProtocolUtils.read_char(dis));
					len = ProtocolUtils.read_int32(dis);
					System.out.print(ProtocolUtils.read_command_variable(dis, len) + "\n");

					command = ProtocolUtils.read_command32(dis);
					System.out.print("Server Response: " + command +
							ProtocolUtils.read_char(dis));
					len = ProtocolUtils.read_int32(dis);
					System.out.print(ProtocolUtils.read_command_variable(dis, len) + "\n");

					/* DELETE ACCOUNT COMMAND */

					ProtocolUtils.write_command32(dos, DELETE_ACCOUNT);

					command = ProtocolUtils.read_command32(dis);
					System.out.println("Server Response: " + command +
							ProtocolUtils.read_char(dis) +
							ProtocolUtils.read_int32(dis));
					assertEquals(ERROR, command); // ERROR 6 USER HAS LOGGED OUT

					/* DISALLOWED COMMAND */

					ProtocolUtils.write_command32(dos, DISALLOWED_COMMAND);
					ProtocolUtils.write_char(dos, ' ');
					ProtocolUtils.write_int32(dos, USERNAME.length());
					ProtocolUtils.write_command_variable(dos, USERNAME);
					ProtocolUtils.write_int32(dos, PASSWORD.length());
					ProtocolUtils.write_command_variable(dos, PASSWORD);

					command = ProtocolUtils.read_command32(dis);
					System.out.println("Server Response: " + command +
							ProtocolUtils.read_char(dis) +
							ProtocolUtils.read_int32(dis));
					assertEquals(ERROR, command);
					
					/* We don't need our test streams anymore */
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
