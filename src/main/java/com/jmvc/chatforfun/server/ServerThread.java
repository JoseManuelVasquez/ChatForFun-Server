package com.jmvc.chatforfun.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import com.jmvc.chatforfun.protocol.IProtocol;
import com.jmvc.chatforfun.protocol.Protocol;

public class ServerThread implements Runnable{
	
	private Socket client;
	private ServerSocket server;
	private IProtocol protocol;
	private static boolean working;
	
	/**
	 * Constructor by default
	 */
	public ServerThread()
	{
		client = null;
		working = true;
	}
	
	/**
	 * Constructor given a socket connection
	 * @param client
	 * @throws SocketException
	 */
	private ServerThread(Socket client) throws SocketException
	{
		this.client = client;
		/* Timeout of 30 seconds */
		client.setSoTimeout(2*60*60*1000);
	}
	
	/**
	 * Start a new thread
	 * @param port
	 * @throws IOException
	 */
	public void startServer(int port) throws IOException
	{
		server = new ServerSocket(port);
		
		 while(working)
         {
             if(!working)
             {
                 server.close();
                 continue;
             }
             
             new Thread(new ServerThread(server.accept())).start();
             
         }
	}

	@Override
	public void run()
	{
		try
		{
			protocol = new Protocol(client);
			while(protocol.readValidCommand());
			client.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

}
