package com.jmvc.chatforfun.model;

public class DTOPendingMessage {

    /* Not Null field */
    private String userName;

    /* Not Null field */
    private String friendName;

    /* Not null field */
    private String message;

    public DTOPendingMessage(String userName, String friendName, String message)
    {
        this.userName = userName;
        this.friendName = friendName;
        this.message = message;
    }

    /* ---------------- GETTERS AND SETTERS ---------------- */

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFriendName() {
        return friendName;
    }

    public void setFriendName(String friendName) {
        this.friendName = friendName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
