package com.gwu.cs6431.service.io;

import com.gwu.cs6431.service.constant.ClientProps;
import javafx.application.Platform;
import javafx.scene.control.Alert;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

import static com.gwu.cs6431.gui.Controller.promptAlert;

public class SocketFactory {
    public static Socket newSocket() {
        Socket socket;
        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress(ClientProps.SERVER_ADDRESS, ClientProps.SERVER_PORT), ClientProps.TIME_OUT);
        } catch (IOException e) {
            promptAlert(Alert.AlertType.ERROR, "Connection Error!"
                    , "Can not connect to server", "Please check your network.");
            Platform.exit();
            return null;
        }
        return socket;
    }
}
