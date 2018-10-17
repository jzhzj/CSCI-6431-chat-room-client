package com.gwu.cs6431.gui;

import com.gwu.cs6431.service.messageHandler.RegHandler;
import com.gwu.cs6431.service.messageHandler.SignHandler;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class InitController extends Controller implements Initializable {
    @FXML
    private Button signInButton;
    @FXML
    private Button signUpButton;
    @FXML
    private Button cancelButton;
    @FXML
    private TextField userIdTxt;
    @FXML
    private PasswordField passwd;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        signInButton.setOnAction(event -> signInAction());

        signUpButton.setOnAction(event -> signUpAction());

        cancelButton.setOnAction(event -> Platform.exit());

        userIdTxt.textProperty().addListener((observable, oldValue, newValue) -> idLengthLimit(oldValue, newValue));

        passwd.textProperty().addListener((observable, oldValue, newValue) -> pdLengthLimit(oldValue, newValue));
    }

    private void signInAction() {
//        if (!checkAccount(userIdTxt.getText(), passwd.getText())) {
//            promptAlert(Alert.AlertType.ERROR, "Wrong Format!"
//                    , "You can only use letters and numbers as your User ID and Password."
//                    , "User ID should starts with a letter. The length should between 5 and 10." + System.lineSeparator()
//                            + "The length of Password should be between 6 and 13.");
//            return;
//        }
//        Socket socket = newSocket();
        SignHandler signHandler = new SignHandler(new Socket(), userIdTxt.getText(), passwd.getText());
        if (/*signHandler.execute()*/true) {
            changeStage();
        } else {
            promptAlert(Alert.AlertType.ERROR, "Failed", signHandler.getServerFeedback(), "Please try again.");
        }
        try {
            signHandler.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void signUpAction() {
        if (!checkAccount(userIdTxt.getText(), passwd.getText())) {
            promptAlert(Alert.AlertType.ERROR, "Wrong Format!"
                    , "You can only use letters and numbers as your User ID and Password."
                    , "User ID should starts with a letter. The length should between 5 and 10." + System.lineSeparator()
                            + "The length of Password should be between 6 and 13.");
            return;
        }
        Socket socket = newSocket();
        RegHandler regHandler = new RegHandler(socket, userIdTxt.getText(), passwd.getText());
        if (regHandler.execute()) {
            promptAlert(Alert.AlertType.INFORMATION, "Success!", regHandler.getServerFeedback()
                    , "Please remember your User ID and Password :)");
            changeStage();
        } else {
            promptAlert(Alert.AlertType.ERROR, "Failed", regHandler.getServerFeedback(), "Please try again.");
        }
        try {
            regHandler.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void idLengthLimit(String oldValue, String newValue) {
        if (newValue.length() > 10)
            userIdTxt.setText(oldValue);
    }

    private void pdLengthLimit(String oldValue, String newValue) {
        if (newValue.length() > 13)
            passwd.setText(oldValue);
    }

    private boolean checkAccount(String id, String pd) {
        // User ID should starts with a letter, followed by letters or numbers.
        // Length should be between 5 and 10.
        String idRegex = "^[a-zA-Z][\\w]{4,9}";
        // Length should be between 6 and 13.
        String pdRegex = "[a-zA-Z\\d]{6,13}";
        return id.matches(idRegex) && pd.matches(pdRegex);
    }

    private void changeStage() {
        Stage initStage = (Stage) signInButton.getScene().getWindow();
        initStage.close();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/gwu/cs6431/gui/Main.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
