package com.jmvc.chatforfun.model;

public class DTOUser {
	
	/* Primary Key */
	private String userName;
	
	/* Not Null field */
	private String password;
	
	public DTOUser(String userName, String password)
	{
		this.userName = userName;
		this.password = password;
	}
	
	/* ---------------- GETTERS AND SETTERS ---------------- */

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	

}
