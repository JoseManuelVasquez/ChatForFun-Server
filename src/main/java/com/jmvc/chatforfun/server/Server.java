package com.jmvc.chatforfun.server;

import java.io.IOException;

/**
 * @author JMVC
 *
 */
public class Server {

	public static void main(String[] args) throws IOException
	{
		try
		{
			if(args[0].equals("-p") && Integer.parseInt(args[1]) > 10000 && Integer.parseInt(args[1]) <= 65535)
			{
				System.out.println("Server running at port: " + args[1]);
				ServerThread server = new ServerThread();
				server.startServer(27500);
				
			}
			else
				System.out.println("Parameters: -p <PORT_NUMBER>");
		}
		catch(ArrayIndexOutOfBoundsException e)
		{
			System.out.println("Wrong parameters");
		}
		catch(NumberFormatException e)
		{
			System.out.println("Port cannot be a string");
		}
	}

}
