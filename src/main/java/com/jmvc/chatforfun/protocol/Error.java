package com.jmvc.chatforfun.protocol;

public final class Error {
	
	private Error() {}
	
	public static final int ERROR_NO_CONNECTION = 0;
	
	public static final int ERROR_USER_OR_PASSWORD_WRONG = 1;
	
	public static final int ERROR_USER_NOT_FOUND = 2;
	
	public static final int ERROR_WHILE_TRYING_TO_ADD = 3;
	
	public static final int ERROR_WHILE_TRYING_TO_DEL = 4;
	
	public static final int ERROR_TIMEOUT = 5;
	
	public static final int ERROR_EXPIRED_SESSION = 6;
	
	public static final int ERROR_USER_EXISTS = 7;
	
	public static final int ERROR_WHILE_TRYING_TO_SEND_MSG = 8;
	
	public static final int ERROR_UNKNOWN = 9;
	
	public static final int ERROR_BAD_COMMAND = 10;

}
