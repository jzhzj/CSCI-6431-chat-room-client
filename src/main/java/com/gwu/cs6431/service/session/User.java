package com.gwu.cs6431.service.session;

public class User {
    private String userID;
    private String passwd;
    private static User clientUser;
    private static boolean isSet;

    public User(String userID) {
        this.userID = userID;
    }

    public User(String userID, String passwd) {
        this(userID);
        this.passwd = passwd;
    }

    public String getUserID() {
        return userID;
    }

    public String getPasswd() {
        return passwd;
    }

    public static void setClientUser(User user) {
        if (!isSet) {
            clientUser = user;
            isSet = true;
        }
    }

    public static User getClientUser() {
        if (isSet) {
            return clientUser;
        }
        return null;
    }
}
