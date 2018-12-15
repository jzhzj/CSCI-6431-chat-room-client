package com.gwu.cs6431.client.service.io;

import com.gwu.cs6431.client.gui.Controller;
import com.gwu.cs6431.client.service.constant.ClientProps;
import javafx.application.Platform;
import javafx.scene.control.Alert;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * This class manages the socket connected with the server.
 * Every object that wants to get a socket must get the socket by this factory.
 *
 * @author qijiuzhi
 */
public class SocketFactory {
    private static Socket socket;

    public static synchronized Socket getConstSocket() {
        if (socket == null) {
            try {
                socket = new Socket();
                socket.connect(new InetSocketAddress(ClientProps.SERVER_ADDRESS, ClientProps.SERVER_PORT), ClientProps.TIME_OUT);
            } catch (IOException e) {
                Controller.promptAlert(Alert.AlertType.ERROR, "Connection Error!"
                        , "Can not connect to server", "Please check your network.");
                Platform.exit();
                return null;
            }
        }

        return socket;
    }

    public static void close() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
