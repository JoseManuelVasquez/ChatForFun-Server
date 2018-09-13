package com.jmvc.chatforfun.server;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * @author JMVC
 *
 */
class AccessDBTest {
	
	private static final String USER = "jose";
	private static final String FRIEND = "pedro";

	@Test
	void accessDB()
	{
		assertFalse(AccessDB.authenticateUser(USER, USER));
		assertFalse(AccessDB.deleteUser(USER, USER));
		assertTrue(AccessDB.registerUser(USER, USER));
		assertFalse(AccessDB.registerUser(USER, USER));
		assertTrue(AccessDB.registerUser(FRIEND, FRIEND));
		assertFalse(AccessDB.deleteFriend(USER, USER, FRIEND));
		assertTrue(AccessDB.addFriend(USER, USER, FRIEND));
		assertTrue(AccessDB.deleteFriend(USER, USER, FRIEND));
		assertTrue(AccessDB.deleteUser(FRIEND, FRIEND));
		assertFalse(AccessDB.deleteUser(FRIEND, FRIEND));
	}

}
