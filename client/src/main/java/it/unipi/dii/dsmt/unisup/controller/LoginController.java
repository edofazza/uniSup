package it.unipi.dii.dsmt.unisup.controller;

import it.unipi.dii.dsmt.unisup.NewMain;
import it.unipi.dii.dsmt.unisup.beans.User;
import it.unipi.dii.dsmt.unisup.communication.AuthGateway;
import it.unipi.dii.dsmt.unisup.communication.Authenticator;
import it.unipi.dii.dsmt.unisup.userinterface.CurrentUI;
import it.unipi.dii.dsmt.unisup.userinterface.PopUp;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.IOException;

public class LoginController {

    @FXML
    private Button loginBtn;
    @FXML
    private Button registerBtn;
    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;

    @FXML
    private void initialize() {
       setActionCommands();
    }

    private void setActionCommands() {
        loginBtn.setOnAction(e ->{
            String username = usernameField.getText();
            String password = passwordField.getText();
            User user = new User(username, password);
            Authenticator au = AuthGateway.getInstance();
            if(au.login(user)) {
                CurrentUI.setUser(user);

                // LOAD THE CHATS
                AuthGateway authGateway = AuthGateway.getInstance();
                user.setChatList(authGateway.getChatHistory(user));
                try {
                    NewMain.getStage().setScene(new Scene(NewMain.loadFXML("MainFrame")));
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
            else{
                PopUp.showPopUpMessage("Login Error", "The username/password is wrong!", "", Alert.AlertType.ERROR);
            }
        });
        registerBtn.setOnAction(e ->{
            String username = usernameField.getText();
            String password = passwordField.getText();
            Authenticator auth = AuthGateway.getInstance();
            User user = new User(username,password);
            boolean result = auth.register(user);

            if (result) {
                PopUp.showPopUpMessage("Registration successfully", "You have been registered to the application.", "", Alert.AlertType.INFORMATION);
            } else {
                PopUp.showPopUpMessage("Registration Error", "The username is already taken.", "Select another.", Alert.AlertType.ERROR);
            }
        });
    }

}
