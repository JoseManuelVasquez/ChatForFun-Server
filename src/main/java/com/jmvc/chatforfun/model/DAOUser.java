package com.jmvc.chatforfun.model;

import static com.jmvc.chatforfun.model.DBConstants.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author JMVC
 *
 */
public class DAOUser implements IDAOUser {
	
	private Database database;
	
	public DAOUser()
	{
		database = Database.getDatabase();
	}

	@Override
	public void createUser(DTOUser user)
	{
		database.executeStatement(insertUser(user.getUserName(), user.getPassword()));
	}

	@Override
	public void createFriend(DTOUser user, String friend)
	{
		database.executeStatement(insertFriend(user.getUserName(), friend));
		database.executeStatement(insertFriend(friend, user.getUserName()));
	}

	@Override
	public void deleteUser(DTOUser user)
	{
		database.executeStatement(deleteUserQuery(user.getUserName(), user.getPassword()));
	}

	@Override
	public void deleteFriend(DTOUser user, String friend)
	{
		database.executeStatement(deleteFriendQuery(user.getUserName(), friend));
		database.executeStatement(deleteFriendQuery(friend, user.getUserName()));
	}

	@Override
	public boolean isFriendOf(DTOUser user, String friend)
	{
		ResultSet resultSet = database.executeQuery(selectFriendOf(user.getUserName()));
		
		try
		{
			while(resultSet.next())
			{
				if(resultSet.getString(FRIEND_FIELD).equals(friend))
					return true;
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		
		return false;
	}

	@Override
	public boolean existsUser(String user)
	{
		ResultSet resultSet = database.executeQuery(selectUser(user));
		List<String> users = new ArrayList<>();
		
		try
		{
			while(resultSet.next())
				users.add(resultSet.getString(USER_FIELD));
			
			if(!users.isEmpty())
			{
				if(users.get(0) != null)
					return true;
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		
		return false;
	}

	@Override
	public boolean existsUser(DTOUser user)
	{
		ResultSet resultSet = database.executeQuery(selectUser(user.getUserName(), user.getPassword()));
		List<String> users = new ArrayList<>();

		try
		{
			while(resultSet.next())
				users.add(resultSet.getString(USER_FIELD));

			if(!users.isEmpty())
			{
				if(users.get(0) != null)
					return true;
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}

		return false;
	}

	@Override
	public List<String> selectFriends(DTOUser user)
	{
		ResultSet resultSet = database.executeQuery(selectFriendOf(user.getUserName()));
		List<String> friends = new ArrayList<>();

		try
		{
			while(resultSet.next())
				friends.add(resultSet.getString(FRIEND_FIELD));
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		
		return friends;
	}
}
