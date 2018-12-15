package com.gwu.cs6431.client.service.session;

import com.gwu.cs6431.client.service.io.SocketFactory;
import com.gwu.cs6431.client.service.handler.TxtHandler;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashMap;
import java.util.Map;

/**
 * Used to represent the session shared between source user and target user.
 * This class is also in charge of managing all sessions.
 *
 * @author qijiuzhi
 */
public class Session {
    private static Map<String, Session> sessionMap = new HashMap<>();

    private String sessionID;
    private User sourceUser;
    private User targetUser;
    private TxtHandler txtHandler;
    private String history = "";
    private String inputCache = "";
    private PropertyChangeSupport support = new PropertyChangeSupport(this);

    private Session(String sessionID, User sourceUser, User targetUser) {
        this.sessionID = sessionID;
        this.sourceUser = sourceUser;
        this.targetUser = targetUser;
        this.txtHandler = new TxtHandler(SocketFactory.getConstSocket());
        sessionMap.put(sessionID, this);
    }

    private Session(String sessionID, String sourceUser, String targetUser) {
        this(sessionID, new User(sourceUser), new User(targetUser));
    }


    //------------------------Static Method-------------------------------------


    public static Session createSession(String sessionID, String sourceUser, String targetUser) {
        Session session = new Session(sessionID, sourceUser, targetUser);
        sessionMap.put(sessionID, session);
        return session;
    }

    public static void removeSession(String sessionId) {
        sessionMap.remove(sessionId);
    }

    public static void removeSession(Session session) {
        removeSession(session.getSessionID());
    }

    public static Session getSession(String sessionId) {
        return sessionMap.get(sessionId);
    }


    //----------------------Member Method---------------------------------------


    public String getSessionID() {
        return sessionID;
    }


    public User getSourceUser() {
        return sourceUser;
    }

    public User getTargetUser() {
        return targetUser;
    }

    public String getHistory() {
        return history;
    }

    public String getInputCache() {
        return inputCache;
    }

    public void setHistory(String history) {
        this.history = history;
    }

    public void setInputCache(String inputCache) {
        this.inputCache = inputCache;
    }

    public void send(String txt) {
        appendHistory(sourceUser.getUserID(), txt);
        txtHandler.send(targetUser.getUserID(), sessionID, txt);
    }

    public void appendHistory(String userID, String newTxt) {
        String oldValue = history;
        StringBuilder sb = new StringBuilder();
        sb.append(history);
        sb.append(userID);
        sb.append(":");
        sb.append(System.lineSeparator());
        sb.append(System.lineSeparator());
        sb.append(newTxt);
        sb.append(System.lineSeparator());
        sb.append("-----------------------");
        sb.append(System.lineSeparator());
        history = sb.toString();
        support.firePropertyChange(sessionID, oldValue, history);
    }

    public void addListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void removeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }

    public void close() {
        removeSession(sessionID);
    }
}
