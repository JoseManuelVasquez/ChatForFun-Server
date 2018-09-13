package com.jmvc.chatforfun.protocol;

import java.net.Socket;

/**
 * @author JMVC
 *
 */
public class UserSocketData {
	
	private boolean isUserLogged;
	private String user, password;
	private Socket socket;

	public UserSocketData(String user, String password)
	{
		isUserLogged = false;
		this.user = user;
		this.password = password;
	}

	public boolean isUserLogged()
	{
		return isUserLogged;
	}
	
	public void setUserLogged(boolean isUserLogged)
	{
		this.isUserLogged = isUserLogged;
	}
	
	public String getUser()
	{
		return user;
	}

	public String getPassword()
	{
		return password;
	}
	
	public Socket getSocket()
	{
		return socket;
	}

	public void setSocket(Socket socket)
	{
		this.socket = socket;
	}

}
