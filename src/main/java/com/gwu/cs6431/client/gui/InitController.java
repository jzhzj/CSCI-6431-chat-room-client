package com.gwu.cs6431.client.gui;

import com.gwu.cs6431.client.service.handler.AbstractHandler;
import com.gwu.cs6431.client.service.handler.RegHandler;
import com.gwu.cs6431.client.service.handler.SignHandler;
import com.gwu.cs6431.client.service.io.listener.Listener;
import com.gwu.cs6431.client.service.session.User;
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
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller that controls the initial stage.
 *
 * @author qijiuzhi
 */
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

        AbstractHandler.setInitController(this);

        Thread listenerThread = new Thread(Listener.getInstance());
        listenerThread.setDaemon(true);
        listenerThread.start();
    }

    private void signInAction() {
        // check if the pattern of user id and password are correct
        if (checkAccount(userIdTxt.getText(), passwd.getText())) {
            // send message to the server
            SignHandler signHandler = new SignHandler(getConstSocket(), userIdTxt.getText(), passwd.getText());
            signHandler.send();
        }
    }

    public void signIn() {
        User.setClientUser(new User(userIdTxt.getText(), passwd.getText()));
        changeStage();
    }

    private void changeStage() {
        Stage initStage = (Stage) signInButton.getScene().getWindow();
        initStage.close();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/gwu/cs6431/client/gui/Main.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void signUpAction() {
        // check if the pattern of user id and password are correct
        if (checkAccount(userIdTxt.getText(), passwd.getText())) {
            RegHandler regHandler = new RegHandler(getConstSocket(), userIdTxt.getText(), passwd.getText());
            regHandler.send();
        }
    }


    private void idLengthLimit(String oldValue, String newValue) {
        if (newValue.length() > 10) {
            userIdTxt.setText(oldValue);
        }
    }

    private void pdLengthLimit(String oldValue, String newValue) {
        if (newValue.length() > 13) {
            passwd.setText(oldValue);
        }
    }

    private boolean checkAccount(String id, String pd) {
        if (id == null || pd == null) {
            return false;
        }

        boolean flag;

        // User ID should starts with a letter, followed by letters or numbers.
        // Length should be between 5 and 10.
        String idRegex = "^[a-zA-Z][\\w]{4,9}";
        // Length should be between 6 and 13.
        String pdRegex = "[a-zA-Z\\d]{6,13}";

        flag = id.matches(idRegex) && pd.matches(pdRegex);

        if (flag) {
            return true;
        } else {
            promptAlert(Alert.AlertType.ERROR, "Wrong Format!"
                    , "You can only use letters and numbers as your User ID and Password."
                    , "User ID should starts with a letter. The length should between 5 and 10." + System.lineSeparator()
                            + "The length of Password should be between 6 and 13.");
            return false;
        }
    }
}
