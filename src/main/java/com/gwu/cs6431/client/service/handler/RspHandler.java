package com.gwu.cs6431.client.service.handler;

import com.gwu.cs6431.client.gui.MainController;
import com.gwu.cs6431.client.service.exception.MessageNotCompletedException;
import com.gwu.cs6431.client.service.io.courier.CourierImpl;
import com.gwu.cs6431.client.service.message.Message;
import com.gwu.cs6431.client.service.session.Session;
import com.gwu.cs6431.client.service.session.User;
import javafx.application.Platform;
import javafx.scene.control.Alert;

import java.io.IOException;
import java.net.Socket;

/**
 * Handles outgoing and incoming RSP messages.
 *
 * @author qijiuzhi
 */
public class RspHandler extends AbstractHandler implements InitMsgHandler, IncomingMsgHandler {
    public RspHandler(Socket socket, String sourceUser, String targetUser) {
        this.courier = new CourierImpl(socket);
        Message msg = new Message(Message.StartLine.RSP);
        msg.setSourceUser(sourceUser);
        msg.setTargetUser(targetUser);
        this.msg = msg;
    }

    RspHandler() {
    }

    @Override
    public void receive(Message msg) {
        Session session;

        switch (msg.getStatus()) {
            case Successful:
                if (User.getClientUser().getUserID().equals(msg.getSourceUser())) {

                    // if the source user in the msg is equals to the client user, then this message is initiated by this client.
                    // So, the source user should be msg.getSourceUser()
                    session = Session.createSession(msg.getSessionID(), msg.getSourceUser(), msg.getTargetUser());
                } else if (User.getClientUser().getUserID().equals(msg.getTargetUser())) {

                    // if the source user in the msg isn't equals to the client user, then this client is invited by the source user in the msg.
                    // So, the source user should be msg.getTargetUser()
                    session = Session.createSession(msg.getSessionID(), msg.getTargetUser(), msg.getSourceUser());
                } else {
                    return;
                }
                Platform.runLater(() -> mainController.createSessionPane(session));
                break;
            case Failed:
                if (User.getClientUser().getUserID().equals(msg.getSourceUser())) {
                    Platform.runLater(() -> MainController.promptAlert(Alert.AlertType.ERROR, "Failed to set up connection", msg.getTxt(),
                            "Sorry, something wrong with the service, we cannot establish your connection with " + msg.getTargetUser()));
                } else if (User.getClientUser().getUserID().equals(msg.getTargetUser())) {
                    Platform.runLater(() -> MainController.promptAlert(Alert.AlertType.ERROR, "Failed to set up connection", msg.getTxt(),
                            "Sorry, something wrong with the service, we cannot establish your connection with " + msg.getSourceUser()));
                }
                break;
            case Refused:
                if (User.getClientUser().getUserID().equals(msg.getSourceUser())) {
                    String feedback = msg.getTxt() == null ? null : msg.getTargetUser() + ": " + msg.getTxt();

                    Platform.runLater(() -> MainController.promptAlert(Alert.AlertType.INFORMATION,
                            "Invitation Refused",
                            msg.getTargetUser() + " refused your invitation",
                            feedback));
                }
                break;
            default:
        }
    }

    @Override
    public void send() {
        if (msg == null) {
            return;
        }
        try {
            courier.send(msg);
        } catch (IOException | MessageNotCompletedException e) {
            e.printStackTrace();
        }
    }

    public void accept() {
        msg.setStatus(Message.Status.Accepted);
        send();
    }

    public void refuse() {
        msg.setStatus(Message.Status.Refused);
        send();
    }
}
