package com.jmvc.chatforfun.model;

import java.util.List;

/**
 * @author JMVC
 *
 */
public interface IDAOUser {
	
	public void createUser(DTOUser user);
	
	public void createFriend(DTOUser user, String friend);
	
	public void deleteUser(DTOUser user);
	
	public void deleteFriend(DTOUser user, String friend);
	
	public boolean existsUser(DTOUser user);
	
	public boolean isFriendOf(DTOUser user, String friend);
	
	public List<String> selectFriends(DTOUser user);

}
