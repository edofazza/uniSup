package it.unipi.dii.dsmt.unisup.controller;

import it.unipi.dii.dsmt.unisup.NewMain;
import it.unipi.dii.dsmt.unisup.beans.User;
import it.unipi.dii.dsmt.unisup.communication.AuthGateway;
import it.unipi.dii.dsmt.unisup.communication.Authenticator;
import it.unipi.dii.dsmt.unisup.userinterface.CurrentUI;
import javafx.fxml.FXML;
import javafx.scene.Scene;
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
            String username = "GOOFIE"; //TODO take the actual username from the TextField
            String password = "GOOFIE"; //TODO take the actual username from the TextField
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
                //TODO display the error label
            }
        });
        registerBtn.setOnAction(e ->{
            String username = "GOOFIE"; //TODO take the actual username from the TextField
            String password = "GOOFIE"; //TODO take the actual username from the TextField
            Authenticator auth = AuthGateway.getInstance();
            User user = new User(username,password);
            boolean result = auth.register(user);

            if (result) {
                //TODO display success label
            } else {
                //TODO display error label
            }
        });
    }

}
