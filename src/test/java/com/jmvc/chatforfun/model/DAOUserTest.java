package com.jmvc.chatforfun.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Iterator;
import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * @author JMVC
 *
 */
class DAOUserTest {
	
	private static final String USER = "jose";
	private static final String FRIEND = "pedro";

	@Test
	void statementsTest()
	{
		DAOUser daoUser = new DAOUser();
		DTOUser user = new DTOUser(USER, USER);
		DTOUser friend = new DTOUser(FRIEND, FRIEND);
		
		daoUser.createUser(user);
		daoUser.createUser(friend);
		daoUser.createFriend(user, FRIEND);
		
		List<String> friendsOfUser = daoUser.selectFriends(user);
		if(friendsOfUser.isEmpty())
		{
			System.out.println("HOLAAA");
		}
		Iterator<String> iterator = friendsOfUser.iterator();
		
		String friendPedro = iterator.next();

		assertTrue(daoUser.existsUser(user.getUserName()));
		assertTrue(daoUser.existsUser(friend.getUserName()));
		assertEquals(FRIEND, friendPedro);
		
	}

}
