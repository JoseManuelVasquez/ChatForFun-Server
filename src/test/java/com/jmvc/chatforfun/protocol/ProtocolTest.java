package com.jmvc.chatforfun.protocol;

import static org.junit.jupiter.api.Assertions.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.junit.jupiter.api.Test;

/**
 * @author JMVC
 *
 */
class ProtocolTest {
	
	private static final String NAME_FILE = "test_file";
	private static final String USERNAME = "jose";
	private static final String PASSWORD = "test";
	private static final String ALLOWED_COMMAND = "STRT";
	private static final String DISALLOWED_COMMAND = "STRX";

	@Test
	void readCommand()
	{
		try
		{
			/* Test File */
			File flux = new File(NAME_FILE);
			flux.createNewFile();
			
			DataInputStream server = new DataInputStream(new FileInputStream(NAME_FILE));
			DataOutputStream client = new DataOutputStream(new FileOutputStream(NAME_FILE));
			
			//Protocol protocol = new Protocol(client, server);
			//TODO
			
			/* STRT allowed command */
			client.writeBytes(ALLOWED_COMMAND);
			client.write(32);
			client.writeBytes(Command.START_TAG);
			client.writeInt(USERNAME.length());
			client.writeBytes(USERNAME);
			client.writeBytes(Command.END_TAG);
			client.writeBytes(Command.START_TAG);
			client.writeInt(PASSWORD.length());
			client.writeBytes(PASSWORD);
			client.writeBytes(Command.END_TAG);
			
			//assertTrue(protocol.readValidCommand());
			
			/* STRT disallowed command */
			client.writeBytes(DISALLOWED_COMMAND);
			client.write(32);
			client.writeBytes(Command.START_TAG);
			client.writeInt(USERNAME.length());
			client.writeBytes(USERNAME);
			client.writeBytes(Command.END_TAG);
			client.writeBytes(Command.START_TAG);
			client.writeInt(PASSWORD.length());
			client.writeBytes(PASSWORD);
			client.writeBytes(Command.END_TAG);
			
			//assertFalse(protocol.readValidCommand());
			
			/* We don't need our test file and streams anymore */
	        flux.delete();
	        server.close();
	        client.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

}
