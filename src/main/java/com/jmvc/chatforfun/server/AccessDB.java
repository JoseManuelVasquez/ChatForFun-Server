package com.jmvc.chatforfun.server;

import java.util.List;

import com.jmvc.chatforfun.model.DAOUser;
import com.jmvc.chatforfun.model.DTOUser;

/**
 * @author JMVC
 *
 */
public final class AccessDB {
	
	private AccessDB() {}
	
	/**
	 * Register a new user
	 * @param user
	 * @param password
	 * @return boolean
	 */
	public static boolean registerUser(String user, String password)
	{
		DAOUser daoUser = new DAOUser();
		DTOUser dtoUser = new DTOUser(user, password);
		if(daoUser.existsUser(dtoUser))
			return false;
		
		daoUser.createUser(dtoUser);
		
		return true;
	}
	
	/**
	 * Authentication given an user and password
	 * @param user
	 * @param password
	 * @return boolean
	 */
	public static boolean authenticateUser(String user, String password)
	{
		DAOUser daoUser = new DAOUser();
		DTOUser dtoUser = new DTOUser(user, password);
		if(!daoUser.existsUser(dtoUser))
			return false;
		
		return true;
	}
	
	/**
	 * Delete a specific user
	 * @param user
	 * @param password
	 * @return boolean
	 */
	public static boolean deleteUser(String user, String password)
	{
		DAOUser daoUser = new DAOUser();
		DTOUser dtoUser = new DTOUser(user, password);
		if(!daoUser.existsUser(dtoUser))
			return false;
		
		daoUser.deleteUser(dtoUser);
		
		return true;
	}
	
	/**
	 * Delete a specific user
	 * @param user
	 * @param password
	 * @param friend
	 * @return boolean
	 */
	public static boolean addFriend(String user, String password, String friend)
	{
		DAOUser daoUser = new DAOUser();
		DTOUser dtoUser = new DTOUser(user, password);
		if(!daoUser.existsUser(dtoUser))
			return false;
		
		daoUser.createFriend(dtoUser, friend);
		
		return true;
	}
	
	/**
	 * Delete a specific friend
	 * @param user
	 * @param password
	 * @param friend
	 * @return boolean
	 */
	public static boolean deleteFriend(String user, String password, String friend)
	{
		DAOUser daoUser = new DAOUser();
		DTOUser dtoUser = new DTOUser(user, password);
		if(!daoUser.existsUser(dtoUser) || !daoUser.isFriendOf(dtoUser, friend))
			return false;
		
		daoUser.deleteFriend(dtoUser, friend);
		
		return true;
	}
	
	public static List<String> getFriendsOf(String user, String password)
	{
		DAOUser daoUser = new DAOUser();
		DTOUser dtoUser = new DTOUser(user, password);
		if(!daoUser.existsUser(dtoUser))
			return null;
		
		List<String> friends = daoUser.selectFriends(dtoUser);
		if(friends.isEmpty())
			return null;
		
		return friends;
	}
}
