package it.unipi.dii.dsmt.unisup.controller;

import it.unipi.dii.dsmt.unisup.Main;
import it.unipi.dii.dsmt.unisup.beans.User;
import it.unipi.dii.dsmt.unisup.communication.AuthGateway;
import it.unipi.dii.dsmt.unisup.communication.Authenticator;
import it.unipi.dii.dsmt.unisup.userinterface.PopUp;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

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
        usernameField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals(""))
                usernameField.setText(newValue);
            else if (!newValue.matches("^[a-zA-Z_0-9]+$")) {
                usernameField.setText(oldValue);
            }
        });

        loginBtn.setOnAction(e ->{
            String username = usernameField.getText();
            String password = passwordField.getText();

            if (username.equals("") || password.equals(""))
                PopUp.showPopUpMessage("Login Error", "Username/Password must be filled", "", Alert.AlertType.ERROR);
            
            User user = new User(username, password); //We instantiate a new User Object for the current user
            Authenticator au = AuthGateway.getInstance();
            if(au.login(user)) {     //Tries to login
                Main.setUser(user);

                // LOAD THE CHATS
                AuthGateway authGateway = AuthGateway.getInstance();
                Main.getUserLogged().setChatList(authGateway.getChatHistory(user));  // Retrieves chat history of the logged user and add them to the chat list.
                                                                                        //So, every time you need them, pick them from the user object
                startReceiveThread();

                Main.changeStage("MainFrame");
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

    private void startReceiveThread(){
        Thread thread = Main.getReceiveThread();
        thread.setDaemon(true);
        thread.start();
    }

}
