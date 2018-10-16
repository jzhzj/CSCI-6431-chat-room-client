package com.gwu.cs6431.service.session;

public class User {
    private String userID;

    public User(String userID) {
        this.userID = userID;
    }

    public String getUserID() {
        return userID;
    }
}
