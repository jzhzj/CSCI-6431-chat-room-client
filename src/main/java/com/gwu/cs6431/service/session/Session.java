package com.gwu.cs6431.service.session;

import com.gwu.cs6431.service.io.courier.CourierImpl;
import com.gwu.cs6431.service.message.Message;
import com.gwu.cs6431.service.messageHandler.CloseAbstractHandler;
import com.gwu.cs6431.service.messageHandler.TxtAbstractHandler;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.net.Socket;

public class Session {
    private String sessionID;
    private Socket socket;
    private User sourceUser;
    private User targetUser;
    private TxtAbstractHandler txtHandler;
    private CloseAbstractHandler closeHandler;
    private String history = "";
    private String inputCache = "";

    private PropertyChangeSupport support = new PropertyChangeSupport(this);

    public Session(String sessionID, Socket socket, User sourceUser, User targetUser) {
        this.sessionID = sessionID;
        this.socket = socket;
        this.sourceUser = sourceUser;
        this.targetUser = targetUser;
        this.txtHandler = new TxtAbstractHandler(socket);
        this.closeHandler = new CloseAbstractHandler(this.socket, this.sessionID
                , this.sourceUser.getUserID(), this.targetUser.getUserID());
        createListenTask();
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
        txtHandler.send(txt);
    }

    private void createListenTask() {
        new Thread(() -> listen()).start();
    }

    private void listen() {
        for (; ; ) {
            String rawStr = null;
            try {
                rawStr = new CourierImpl(socket).listen();
            } catch (IOException e) {
                // TODO This might be a bug. Verify it later
                break;
            }
            handleMsg(rawStr);
        }
    }

    private void handleMsg(String rawStr) {
        Message msg = Message.genMessage(rawStr);
        if (msg == null) {
            return;
        }
        if (Message.StartLine.TXT.equals(msg.getStartLine())) {
            if (sessionID.equals(msg.getSessionID())
                    && targetUser.getUserID().equals(msg.getSourceUser())
                    && sourceUser.getUserID().equals(msg.getTargetUser())) {
                appendHistory(targetUser.getUserID(), msg.getTxt());
            }
        } else if (Message.StartLine.CLOSE.equals(msg.getStartLine())) {
            if (sessionID.equals(msg.getSessionID())
                    && targetUser.getUserID().equals(msg.getSourceUser())
                    && sourceUser.getUserID().equals(msg.getTargetUser())) {
                closeResource();
            }
        }
    }

    private void appendHistory(String userID, String newTxt) {
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
        closeHandler.send();
        closeResource();
    }

    private void closeResource() {
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
