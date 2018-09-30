package com.jmvc.chatforfun.model;

import static com.jmvc.chatforfun.model.DBConstants.*;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author JMVC
 *
 */
public final class Database{
	
	/* Attributes */
	private static Connection connection;
	private volatile static Database database;
	
	private Database() {}
	
	public static Database getDatabase()
	{
		if (database == null)
		{
			synchronized(Database.class)
			{
				if (database == null)
				{
					try
					{
						database = new Database();
						connection = DriverManager.getConnection(DATABASE_CONNECTION);

						/* We need a non-volatile database */
						File file = new File(DATABASE_NAME);
						if (!file.exists())
							createTables();
					}
					catch (SQLException e)
					{
						e.printStackTrace();
					}
				}
			}
		}
		
		return database;
	}
	
	/**
	 * Method for executing a SQL command
	 * @param sql
	 */
	public void executeStatement(String sql)
	{
		if (database != null)
		{
			synchronized(Database.class)
			{
				if (database != null)
				{
					try
					{
						Statement statement = connection.createStatement();
						statement.setQueryTimeout(TIMEOUT);
						
						statement.executeUpdate(sql);
						statement = null;
					}
					catch (SQLException e)
					{
						e.printStackTrace();
					}
				}
			}
		}
		
	}
	
	/**
	 * Method for executing a SQL command
	 * @param query
	 */
	public ResultSet executeQuery(String query)
	{
		if (database != null)
		{
			try
			{
				Statement statement = connection.createStatement();
				statement.setQueryTimeout(TIMEOUT);
				ResultSet resultSet = statement.executeQuery(query);
				statement = null;
				
				return resultSet;
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
		}
		
		return null;
	}
	
	/**
	 * Destroy unique instance
	 */
	public static void destroyInstance()
	{
		database = null;
	}

	/**
	 * Create all statements for tables
	 * @throws SQLException 
	 */
	private static void createTables() throws SQLException
	{
		Statement statement = connection.createStatement();
		statement.setQueryTimeout(TIMEOUT);

		/* We first create new tables after dropping tables */
		statement.executeUpdate(DROP_TABLE_USER);
		statement.executeUpdate(CREATE_TABLE_USER);
		statement.executeUpdate(DROP_TABLE_FRIEND);
		statement.executeUpdate(CREATE_TABLE_FRIEND);
        statement.executeUpdate(DROP_TABLE_MESSAGE);
        statement.executeUpdate(CREATE_TABLE_MESSAGE);
	}

	/**
	 * Reset our database
	 */
	public void resetDatabase()
	{
		if (database != null)
		{
			try
			{
				createTables();
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
		}
	}
}
