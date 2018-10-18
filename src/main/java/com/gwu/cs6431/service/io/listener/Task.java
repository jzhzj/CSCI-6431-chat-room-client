package com.gwu.cs6431.service.io.listener;

import com.gwu.cs6431.gui.MainController;
import com.gwu.cs6431.service.io.SocketFactory;
import com.gwu.cs6431.service.message.Message;
import com.gwu.cs6431.service.messageHandler.RspHandler;
import com.gwu.cs6431.service.session.Session;
import com.gwu.cs6431.service.session.User;
import javafx.scene.control.Alert;

import java.io.IOException;
import java.net.Socket;

/**
 * This class is used to handle received messages from Listener.
 *
 * */
public class Task implements Runnable {
    private String messageStr;
    private MainController mainController;

    public Task(MainController mainController, String messageStr) {
        this.mainController = mainController;
        this.messageStr = messageStr;
    }

    @Override
    public void run() {
        Message msg = Message.genMessage(messageStr);
        if (msg == null)
            return;
        if (!Message.StartLine.INVT.equals(msg.getStartLine()))
            return;
        handle(msg);
    }

    private void handle(Message msg) {
        // promptAlert invitation from GUI
        String txt = null;
        if (msg.getTxt() != null)
            txt = msg.getSourceUser() + ": " + msg.getTxt();
        boolean accepted = MainController.promptINVT(Alert.AlertType.NONE
                , "New Invitation"
                , msg.getSourceUser() + " would like to chat with you."
                , txt);

        // Create new socket. If success to connect to remote user, this socket will be used in long term
        Socket socket = SocketFactory.newSocket();
        RspHandler rspHandler = new RspHandler(socket, msg.getSourceUser(), msg.getTargetUser());
        if (accepted) {
            // If successfully connect to the server
            if (rspHandler.accept())
                // Create new Session pane in GUI
                mainController.createSessionPane(new Session(rspHandler.getSessionID(), socket, User.getClientUser().getUserID(), rspHandler.getRemoteUser()));
            else {
                // else, prompt an alert
                MainController.promptAlert(Alert.AlertType.ERROR, "Connection Error"
                        , "Failed to establish connection with server."
                        , "Please check your network.");
                // then close the resource
                try {
                    rspHandler.close();
                } catch (IOException e) {
                    // TODO
                }
            }
        } else {
            // if refuse the invitation
            rspHandler.refuse();
            try {
                // close resource
                rspHandler.close();
            } catch (IOException e) {
                // TODO
            }
        }
    }
}
