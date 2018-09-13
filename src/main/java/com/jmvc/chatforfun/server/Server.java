package com.jmvc.chatforfun.server;

import java.io.IOException;

/**
 * @author JMVC
 *
 */
public class Server {

	public static void main(String[] args) throws IOException
	{
		ServerThread server = new ServerThread();
		server.startServer(27500);
		
	}

}
