package com.gwu.cs6431.service.session;

import com.gwu.cs6431.service.messageHandler.CloseHandler;
import com.gwu.cs6431.service.messageHandler.TxtHandler;

import java.io.IOException;
import java.net.Socket;

public class Session {
    private String sessionID;
    private Socket socket;
    private User sourceUser;
    private User targetUser;
    private TxtHandler txtHandler;
    private CloseHandler closeHandler;

    public Session(String sessionID, Socket socket, User sourceUser, User targetUser) {
        this.sessionID = sessionID;
        this.socket = socket;
        this.sourceUser = sourceUser;
        this.targetUser = targetUser;
        this.txtHandler = new TxtHandler(socket);
        this.closeHandler = new CloseHandler(this.socket, this.sessionID
                , this.sourceUser.getUserID(), this.targetUser.getUserID());
    }

    public Session(String sessionID, Socket socket, String sourceUser, String targetUser) {
        this(sessionID, socket, new User(sourceUser), new User(targetUser));
    }

    public String getSessionID() {
        return sessionID;
    }

    public Socket getSocket() {
        return socket;
    }

    public User getSourceUser() {
        return sourceUser;
    }

    public User getTargetUser() {
        return targetUser;
    }

    public void send(String txt) {
        txtHandler.send(txt);
    }

    public void listenMsg() {
        
    }

    public void close() {
        closeHandler.send();
        try {
            txtHandler.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            closeHandler.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
