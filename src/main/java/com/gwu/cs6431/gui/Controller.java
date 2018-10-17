package com.gwu.cs6431.gui;

import com.gwu.cs6431.service.io.SocketFactory;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.input.MouseEvent;

import java.net.Socket;
import java.util.Optional;

public class Controller {
    Socket newSocket() {
        return SocketFactory.newSocket();
    }

    public static void promptAlert(Alert.AlertType alertType, String title, String headerText, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

    public static boolean promptINVT(Alert.AlertType alertType, String title, String headerText, String contentText) {
        ButtonType[] buttons = new ButtonType[2];
        buttons[0] = new ButtonType("Accept");
        buttons[1] = new ButtonType("Refuse");
        Alert alert = new Alert(alertType, contentText, buttons);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        Optional<ButtonType> result = alert.showAndWait();
        if (!result.isPresent()) {
            return false;
        } else {
            switch (result.get().getText()) {
                case "Accept":
                    return true;
                case "Refuse":
                    return false;
                default:
                    return false;
            }
        }
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
