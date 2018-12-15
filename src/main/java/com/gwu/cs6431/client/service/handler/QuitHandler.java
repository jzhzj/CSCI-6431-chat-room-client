package com.gwu.cs6431.client.service.handler;

import com.gwu.cs6431.client.gui.MainController;
import com.gwu.cs6431.client.service.exception.MessageNotCompletedException;
import com.gwu.cs6431.client.service.io.courier.CourierImpl;
import com.gwu.cs6431.client.service.message.Message;
import javafx.application.Platform;
import javafx.scene.control.Alert;

import java.io.IOException;
import java.net.Socket;

/**
 * The QuitHandler is used to send a QUIT message to the server, when the user wants to log out.
 *
 * @author qijiuzhi
 */
public class QuitHandler extends AbstractHandler implements InitMsgHandler, IncomingMsgHandler {
    QuitHandler() {
    }

    public QuitHandler(Socket socket, String userID, String passwd) {
        this.courier = new CourierImpl(socket);
        Message msg = new Message(Message.StartLine.QUIT);
        msg.setUserID(userID);
        msg.setPasswd(passwd);
        this.msg = msg;
    }

    @Override
    public void send() {
        try {
            courier.send(msg);
        } catch (IOException | MessageNotCompletedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void receive(Message msg) {
        Platform.runLater(() -> MainController.promptAlert(Alert.AlertType.INFORMATION, "You are logged out by the server.", msg.getTxt(), null));
        Platform.exit();
    }
}
