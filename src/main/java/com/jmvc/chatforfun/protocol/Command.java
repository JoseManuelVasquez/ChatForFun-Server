package com.jmvc.chatforfun.protocol;

public final class Command {
	
	private Command() {}
	
	public static final String REGISTER = "RGST";
	
	public static final String LOGIN = "LOIN";
	
	public static final String LOGOUT = "LOUT";
	
	public static final String SEND_MESSAGE = "SEND";
	
	public static final String ADD_FRIEND = "ADDF";
	
	public static final String DELETE_FRIEND = "DELF";
	
	public static final String DELETE_ACCOUNT = "DELA";
	
	public static final String LOGGED_IN = "LGGD";
	
	public static final String MESSAGE_SENT = "SDED";
	
	public static final String MESSAGE_RECEIVED  = "RVED";
	
	public static final String FRIEND_ADDED = "ADED";
	
	public static final String FRIEND_DELETED = "DELD";
	
	public static final String FRIEND_LOGGED_IN = "FDIN";
	
	public static final String FRIEND_LOGGED_OUT = "FOUT";
	
	public static final String ERROR  = "ERRO";
	
	public static final String EXIT = "EXIT";
	
	public static final String[] VALID_COMMANDS = {"RGST", "LOIN", "LOUT", "SEND",
												   "ADDF", "DELF", "DELA", "LGGD",
												   "SDED", "RVED", "ADED", "DELD",
												   "FDIN", "FOUT", "ERRO", "EXIT"};
	
	public static final String START_TAG = "" + (char)6 + (char)7 + (char)6 + (char)7;
	
	public static final String END_TAG = "" + (char)7 + (char)8 + (char)7 + (char)8;

}
