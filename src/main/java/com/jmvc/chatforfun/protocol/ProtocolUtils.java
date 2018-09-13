package com.jmvc.chatforfun.protocol;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author JMVC
 *
 */
public abstract class ProtocolUtils {
    
	/**
     * Method for writing a variable length command to client
     * @param str
     * @throws IOException 
     */
    public static void write_command_variable(DataOutputStream dos, String str) throws IOException
    {
        int lenStr = str.length();
        byte bStr[] = new byte[lenStr];

        for(int i = 0; i < lenStr; i++)
        	bStr[i] = (byte) str.charAt(i);

        dos.write(bStr, 0, lenStr);
    }
	
    /**
     * Method for writing a command 32-bit to client
     * @param str
     * @throws IOException 
     */
    public static void write_command32(DataOutputStream dos, String str) throws IOException
    {
        byte bStr[] = new byte[4];

        for(int i = 0; i < str.length(); i++)
        	bStr[i] = (byte) str.charAt(i);

        /* If we write a less than 4 bytes command */
        for(int i = str.length(); i < 4; i++)
        	bStr[i] = (byte) ' ';

        dos.write(bStr, 0, 4);
    }
    
    /**
     * Method for writing 32-bit number
     * @param number
     * @throws IOException 
     */
    public static void write_int32(DataOutputStream dos, int number) throws IOException
    {
        byte bytes[]=new byte[4];

        int32ToBytes(number,bytes,"be");
        dos.write(bytes, 0, 4);
    }
    
    /**
     * Method for writing character
     * @throws IOException 
     */
    public static void write_char(DataOutputStream dos, char c) throws IOException
    {
        dos.write((byte) c);
    }
    
    /**
     * Method for reading a variable command from client
     * @param bytes
     * @return String
     * @throws IOException 
     */
    public static String read_command_variable(DataInputStream dis, int length) throws IOException
    {
        String str = "";
        byte bStr[] = new byte[length];

        bStr = read_bytes(dis, length);

        for(int i = 0; i < length; i++)
        	str += (char)bStr[i];

        return str; 
    }
    
    /**
     * Method for reading a 32-bit command from client
     * @param bytes
     * @return String
     * @throws IOException 
     */
    public static String read_command32(DataInputStream dis) throws IOException
    {
        return read_command_variable(dis, 4);
    }
    
    /**
     * Method for reading 32 bits number
     * @return int
     * @throws IOException 
     */
    public static int read_int32(DataInputStream dis) throws IOException
    {
        byte bytes[] = new byte[4];
        bytes  = read_bytes(dis, 4);

        return bytesToInt32(bytes,"be");
    }
    
    /**
     * Method for reading character
     * @return char
     * @throws IOException 
     */
    public static char read_char(DataInputStream dis) throws IOException
    {
        byte bStr[] = new byte[1];
        char cStr[] = new char[1];

        bStr = read_bytes(dis, 1);
        cStr[0]= (char) bStr[0];

        return ((char) cStr[0]);
    }
    
    /**
     * Method for reading bytes from a Stream
     * @param numBytes
     * @return byte[]
     * @throws IOException 
     */
    private static byte[] read_bytes(DataInputStream dis, int numBytes) throws IOException
    {
        int len=0 ;
        byte bStr[] = new byte[numBytes];
        do
        {
          len += dis.read(bStr, len, numBytes-len);
        }while (len < numBytes);
        
        return bStr;
    }
    
    /**
     * Method for changing integer to bytes
     * @param number
     * @param bytes
     * @param endianess
     * @return int
     */
    public static int int32ToBytes(int number,byte bytes[], String endianess)
    {
        if("be".equals(endianess.toLowerCase()))
        {
          bytes[0] = (byte)((number >> 24) & 0xFF);
          bytes[1] = (byte)((number >> 16) & 0xFF);
          bytes[2] = (byte)((number >> 8) & 0xFF);
          bytes[3] = (byte)(number & 0xFF);
        }
        else
        {
          bytes[0] = (byte)(number & 0xFF);
          bytes[1] = (byte)((number >> 8) & 0xFF);
          bytes[2] = (byte)((number >> 16) & 0xFF);
          bytes[3] = (byte)((number >> 24) & 0xFF);
        }
        return 4;
    }
    
    /**
     * Method for changing bytes to integer
     * @param bytes
     * @param endianess
     * @return int
     */
    public static int bytesToInt32(byte bytes[], String endianess)
    {
        int number;

        if("be".equals(endianess.toLowerCase()))
        {
          number=((bytes[0] & 0xFF) << 24) | ((bytes[1] & 0xFF) << 16) |
            ((bytes[2] & 0xFF) << 8) | (bytes[3] & 0xFF);
        }
        else
        {
          number=(bytes[0] & 0xFF) | ((bytes[1] & 0xFF) << 8) |
            ((bytes[2] & 0xFF) << 16) | ((bytes[3] & 0xFF) << 24);
        }
        return number;
    }

}
