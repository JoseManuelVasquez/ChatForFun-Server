package com.jmvc.chatforfun.model;

/**
 * @author JMVC
 *
 */
public final class DBConstants {
	
	private DBConstants() {}

	public static final String DATABASE_CONNECTION = "jdbc:sqlite:chatforfun.db";

	public static final int TIMEOUT = 30;
	
	public static final String USER_FIELD = "username";

	public static final String FRIEND_FIELD = "friend";

	public static final String MESSAGE_FIELD = "msg";

	public static final String DROP_TABLE_USER = "DROP TABLE IF EXISTS user";
	
	public static final String DROP_TABLE_FRIEND = "DROP TABLE IF EXISTS friend";

	public static final String DROP_TABLE_MESSAGE = "DROP TABLE IF EXISTS message";
	
	public static final String CREATE_TABLE_USER = "CREATE TABLE user"
													+ "(username varchar PRIMARY KEY, password varchar NOT NULL)";
	
	public static final String CREATE_TABLE_FRIEND = "CREATE TABLE friend"
													+ "(username varchar, friend varchar, PRIMARY KEY(username, friend),"
													+ "FOREIGN KEY(username) REFERENCES user(username) ON DELETE CASCADE,"
													+ "FOREIGN KEY(friend) REFERENCES user(username) ON DELETE CASCADE)";

	public static final String CREATE_TABLE_MESSAGE = "CREATE TABLE message"
													+ "(username varchar NOT NULL, friend varchar NOT NULL, msg varchar NOT NULL)";
	
	public static final String insertUser(String user, String password)
	{
		return "INSERT INTO user VALUES(\"" + user + "\",\"" + password + "\")";
	}
	
	public static final String insertFriend(String user, String friend)
	{
		return "INSERT INTO friend VALUES(\"" + user + "\",\"" + friend + "\")";
	}
	
	public static final String selectUser(String user)
	{
		return "SELECT username FROM user WHERE username = \"" + user + "\"";
	}

	public static final String selectUser(String user, String password)
	{
		return "SELECT username FROM user WHERE username = \"" + user + "\" and password = \"" + password + "\"";
	}
	
	public static final String selectFriendOf(String user)
	{
		return "SELECT friend FROM friend WHERE username = \"" + user + "\"";
	}
	
	public static final String deleteUserQuery(String user, String password)
	{
		return "DELETE FROM user WHERE username = \"" + user + "\" and password = \"" + password + "\"";
	}
	
	public static final String deleteFriendQuery(String user, String friend)
	{
		return "DELETE FROM friend WHERE username = \"" + user + "\" and friend = \"" + friend + "\"";
	}

	public static final String insertMessage(String user, String friend, String message)
	{
		return "INSERT INTO message VALUES(\"" + user + "\",\"" + friend + "\",\"" + message + "\")";
	}

	public static final String selectMessages(String user)
	{
		return "SELECT msg FROM message WHERE username = \"" + user + "\"";
	}

	public static final String selectFriendMessages(String user)
	{
		return "SELECT friend FROM message WHERE username = \"" + user + "\"";
	}

	public static final String deleteMessages(String user)
	{
		return "DELETE FROM message WHERE username = \"" + user + "\"";
	}

}
