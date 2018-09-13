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
class ProtocolUtilsTest {
	
	private static final String NAME_FILE = "test_file";
	private static final int NUMBER_TEST = 0xFF000000 | 0x00BB0000 | 0x0000BD00 | 0x000000AC;
	private static final String COMMAND_NAME_VARIABLE = "STRTXXX";
	private static final String COMMAND_TAG = Command.START_TAG;
	private static final char CHAR_TEST = 'C';

	@Test
	void readTest()
	{
		try
		{
			/* Test File */
			File flux = new File(NAME_FILE);
			flux.createNewFile();
			
			DataInputStream server = new DataInputStream(new FileInputStream(NAME_FILE));
			DataOutputStream client = new DataOutputStream(new FileOutputStream(NAME_FILE));
			
			/* ----------------- INT 32-BIT READING TEST ----------------- */
			/* Client output */
			byte bytesOut[]=new byte[4];
	        ProtocolUtils.int32ToBytes(NUMBER_TEST,bytesOut,"be");
	        client.write(bytesOut, 0, 4);
			
			/* Server input */
	        int result;
	        result = ProtocolUtils.read_int32(server);
	        assertTrue(result == NUMBER_TEST);
	        
	        
	        /* ----------------- STRING VARIABLE READING TEST ----------------- */
	        /* Client output */
	        int numBytes;
	        bytesOut = new byte[7];

	        numBytes = 7;

	        for(int i = 0; i < numBytes; i++)
	        	bytesOut[i] = (byte) COMMAND_NAME_VARIABLE.charAt(i);

	        for(int i = numBytes; i < 7; i++)
	        	bytesOut[i] = (byte) ' ';

	        client.write(bytesOut, 0, 7);
	        
	        /* Server input */
	        String commandReceived = ProtocolUtils.read_command_variable(server, 7);
	        assertEquals(commandReceived, COMMAND_NAME_VARIABLE);
	        
	        /* ----------------- STRING 32-BIT READING TEST ----------------- */
	        /* Client output */
	        client.writeBytes(COMMAND_TAG);
	        
	        /* Server input */
	        commandReceived = ProtocolUtils.read_command32(server);
	        assertEquals(commandReceived, COMMAND_TAG);
	        
	        
	        /* ----------------- CHAR 8-BIT READING TEST ----------------- */
	        /* Client output */
	        client.write((byte)CHAR_TEST);
	        
	        /* Server input */
	        char character = ProtocolUtils.read_char(server);
	        assertTrue(character == CHAR_TEST);
	        
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
	
	@Test
	void writeTest()
	{
		try
		{
			/* Test File */
			File flux = new File(NAME_FILE);
			flux.createNewFile();
			
			DataOutputStream server = new DataOutputStream(new FileOutputStream(NAME_FILE));
			DataInputStream client = new DataInputStream(new FileInputStream(NAME_FILE));
			
			/* ----------------- INT 32-BIT WRITING TEST ----------------- */
			/* Server output */
	        ProtocolUtils.write_int32(server, NUMBER_TEST);
			
			/* Client input */
			int len=0 ;
	        byte bytesIn[] = new byte[4];
	        do
	        {
	          len += client.read(bytesIn, len, 4-len);
	        }while(len < 4);

			assertTrue((byte)0xFF == bytesIn[0]);
			assertTrue((byte)0xBB == bytesIn[1]);
			assertTrue((byte)0xBD == bytesIn[2]);
			assertTrue((byte)0xAC == bytesIn[3]);
			
			
			/* ----------------- STRING VARIABLE WRITING TEST ----------------- */
			/* Server output */
	        ProtocolUtils.write_command_variable(server, COMMAND_NAME_VARIABLE);
			
			/* Client input */
	        String commandReceived;
	        char cStr[] = new char[7];
	        bytesIn = new byte[7];
	        len=0;

	        do
	        {
	          len += client.read(bytesIn, len, 7-len);
	        }while(len < 7);

	        for(int i = 0; i < 7; i++)
	          cStr[i]= (char) bytesIn[i];

	        commandReceived = String.valueOf(cStr).trim();
	        
	        assertEquals(commandReceived, COMMAND_NAME_VARIABLE);
	        
			
			/* ----------------- STRING 32-BIT WRITING TEST ----------------- */
			/* Server output */
	        ProtocolUtils.write_command32(server, COMMAND_TAG);
			
			/* Client input */
	        commandReceived = "";
	        len=0;

	        do
	        {
	          len += client.read(bytesIn, len, 4-len);
	        }while(len < 4);
	        

	        for(int i = 0; i < 4; i++)
	        	commandReceived += (char)bytesIn[i];
	        
	        assertEquals(commandReceived, COMMAND_TAG);
	        
	        
	        /* ----------------- CHAR 8-BIT WRITING TEST ----------------- */
			/* Server output */
	        ProtocolUtils.write_char(server, CHAR_TEST);
	        
	        /* Client input */
	        cStr = new char[1];
	        len=0;

	        do
	        {
	          len += client.read(bytesIn, len, 1-len);
	        }while(len < 1);

	        cStr[0] = (char) bytesIn[0];
	        
	        assertTrue(cStr[0] == CHAR_TEST);
	        
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
