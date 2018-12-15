package com.gwu.cs6431.client.service.handler;

import com.gwu.cs6431.client.service.exception.MessageNotCompletedException;
import com.gwu.cs6431.client.service.io.courier.CourierImpl;
import com.gwu.cs6431.client.service.message.Message;
import javafx.application.Platform;

import java.io.IOException;
import java.net.Socket;

/**
 * Handles outgoing and incoming CLOSE messages.
 *
 * @author qijiuzhi
 */
public class CloseHandler extends AbstractHandler implements InitMsgHandler, IncomingMsgHandler {

    public CloseHandler(Socket socket, String sessionID, String sourceUser, String targetUser) {
        this.courier = new CourierImpl(socket);
        Message msg = new Message(Message.StartLine.CLOSE);
        msg.setSessionID(sessionID);
        msg.setSourceUser(sourceUser);
        msg.setTargetUser(targetUser);
        this.msg = msg;
    }

    CloseHandler() {
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

    @Override
    public void receive(Message msg) {
        Platform.runLater(() -> mainController.closeByOtherUser(msg.getSessionID()));
    }
}
