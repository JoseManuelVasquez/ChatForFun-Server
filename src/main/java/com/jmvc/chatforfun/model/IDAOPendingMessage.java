package com.jmvc.chatforfun.model;

import java.util.List;

public interface IDAOPendingMessage {

    public void createMessage(DTOPendingMessage message);

    public void deleteAllMessages(String user);

    public List<String> selectAllMessages(String user);

    public List<String> selectAllFriends(String user);
}
