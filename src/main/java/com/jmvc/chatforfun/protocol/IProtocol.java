package com.jmvc.chatforfun.protocol;

/**
 * @author JMVC
 *
 */
public interface IProtocol {
	
	/* --------------------------------------- PROTOCOL READING METHODS --------------------------------------- */
	
	public boolean readValidCommand();
	
	/* --------------------------------------- PROTOCOL WRITING METHODS --------------------------------------- */
	
	public void writeRGTDCommand();

	public void writeLGGDCommand();

	public void writeFRDSCommand();

	public void writeNFDSCommand();
	
	public void writeSDEDCommand(String friend);
	
	public void writeRVEDCommand(String friend, String message);
	
	public void writeADEDCommand(String friend);
	
	public void writeDELDCommand(String friend);
	
	public void writeFDINCommand(String user);
	
	public void writeFOUTCommand(String user);
	
	public void writeERROCommand(int errorCode);
	
}
