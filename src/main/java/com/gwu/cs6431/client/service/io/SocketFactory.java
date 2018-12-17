package com.gwu.cs6431.client.service.io;

import com.gwu.cs6431.client.gui.InitController;
import com.gwu.cs6431.client.gui.MainController;
import com.gwu.cs6431.client.service.constant.ClientProps;
import javafx.application.Platform;
import javafx.scene.control.Alert;

import java.io.BufferedReader;
import java.io.FileReader;
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
                socket.connect(new InetSocketAddress(getServerAddr(), ClientProps.SERVER_PORT), ClientProps.TIME_OUT);
            } catch (IOException e) {
                Platform.runLater(() -> MainController.promptAlert(Alert.AlertType.ERROR, "Connection Error!",
                        "Can not connect to server", "Please check your network. Or you may want to check the config file."));
                return null;
            }
        }

        return socket;
    }

    private static String getServerAddr() {
        String configPath = System.getProperty("user.dir") + "/config.txt";
        System.out.println(configPath);
        try (BufferedReader reader = new BufferedReader(new FileReader(configPath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("#")) {
                    continue;
                }
                if (line.startsWith("SERVER_ADDRESS=")) {
                    return line.substring(15);
                }
            }
            throw new IOException();
        } catch (IOException e) {
            Platform.runLater(() -> InitController.promptAlert(Alert.AlertType.ERROR, "Configuration Failure",
                    "Failed to read the configuration file.",
                    "Please read the user guide, and restart the client."));
        }
        return null;
    }

    public static void close() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
