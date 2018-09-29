package com.jmvc.chatforfun.model;

import static com.jmvc.chatforfun.model.DBConstants.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DAOPendingMessage implements IDAOPendingMessage {

    private Database database;

    public DAOPendingMessage()
    {
        database = Database.getDatabase();
    }

    @Override
    public void createMessage(DTOPendingMessage message)
    {
        database.executeStatement(insertMessage(message.getUserName(), message.getFriendName(), message.getMessage()));
    }

    @Override
    public void deleteAllMessages(String user)
    {
        database.executeStatement(deleteMessages(user));
    }

    @Override
    public List<String> selectAllMessages(String user)
    {
        ResultSet resultSet = database.executeQuery(selectMessages(user));
        List<String> messages = new ArrayList<>();

        try
        {
            while(resultSet.next())
                messages.add(resultSet.getString(MESSAGE_FIELD));
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return messages;
    }

    @Override
    public List<String> selectAllFriends(String user)
    {
        ResultSet resultSet = database.executeQuery(selectFriendMessages(user));
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
