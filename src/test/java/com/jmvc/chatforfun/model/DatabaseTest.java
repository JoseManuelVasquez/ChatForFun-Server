package com.jmvc.chatforfun.model;

import static com.jmvc.chatforfun.model.DBConstants.*;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;

import com.anarsoft.vmlens.concurrent.junit.ConcurrentTestRunner;

/* Run in 4 parallel threads */
@RunWith(ConcurrentTestRunner.class)
class DatabaseTest {
	
	private static final String USER = "jose";
	private static final String FRIEND = "pedro";

	@Test
	void databaseConnection()
	{
		Database database = Database.getDatabase();
		
		/* Avoiding a race condition */
		for(int i=0; i<30; i++)
		{
			database.executeStatement(DROP_TABLE_USER);
			database.executeStatement(CREATE_TABLE_USER);
			database.executeStatement(insertUser(USER, USER));
			database.executeStatement(insertUser(FRIEND, FRIEND));
			database.executeStatement(DROP_TABLE_FRIEND);
			database.executeStatement(CREATE_TABLE_FRIEND);
			database.executeStatement(insertFriend(USER, FRIEND));
		}
		
		ResultSet rs = database.executeQuery(selectFriendOf(USER));

		try
		{
			while(rs.next())
				assertEquals(FRIEND, rs.getString(FRIEND_FIELD));
				
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		
	}


}
