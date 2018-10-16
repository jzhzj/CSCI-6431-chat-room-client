package com.gwu.cs6431.gui;

import com.gwu.cs6431.service.constant.ClientProps;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Controller {
     Socket newSocket() {
        Socket socket;
        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress(ClientProps.SERVER_ADDRESS, ClientProps.SERVER_PORT), ClientProps.TIME_OUT);
        } catch (IOException e) {
            prompt(Alert.AlertType.ERROR, "Connection Error!"
                    , "Can not connect to server", "Please check your network.");
            Platform.exit();
            return null;
        }
        return socket;
    }

    public static void prompt(Alert.AlertType alertType, String title, String headerText, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

    @FXML
    private void handleMouseEntered(MouseEvent mouseEvent) {
        Button btn = (Button) mouseEvent.getSource();
        btn.setEffect(new DropShadow());
    }

    @FXML
    private void handleMouseExited(MouseEvent mouseEvent) {
        Button btn = (Button) mouseEvent.getSource();
        btn.setEffect(null);
    }

    @FXML
    private void handleMousePressed(MouseEvent mouseEvent) {
        Button btn = (Button) mouseEvent.getSource();
        btn.setEffect(new InnerShadow());
    }

    @FXML
    private void handleMouseReleased(MouseEvent mouseEvent) {
        Button btn = (Button) mouseEvent.getSource();
        btn.setEffect(new DropShadow());
    }
}
