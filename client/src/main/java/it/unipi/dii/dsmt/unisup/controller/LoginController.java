package it.unipi.dii.dsmt.unisup.controller;

import it.unipi.dii.dsmt.unisup.NewMain;
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
        loginBtn.setOnAction(e ->{
            String username = usernameField.getText();
            String password = passwordField.getText();
            
            User user = new User(username, password); //We instantiate a new User Object for the current user
            Authenticator au = AuthGateway.getInstance();
            if(au.login(user)) {     //Tries to login
                NewMain.setUser(user); //TODO set the logged user as current user in a data structure (better if static)

                // LOAD THE CHATS
                AuthGateway authGateway = AuthGateway.getInstance();
                NewMain.getUserLogged().setChatList(authGateway.getChatHistory(user));  // Retrieves chat history of the logged user and add them to the chat list.
                                                                                        //So, every time you need them, pick them from the user object
                startReceiveThread();

                //TODO if I'm forgetting something at login time, please add it. If not, remove this TODO
                NewMain.changeStage("MainFrame");
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

            //TODO if I'm forgetting something at registration time, please add it. If not, remove this TODO

            if (result) {
                PopUp.showPopUpMessage("Registration successfully", "You have been registered to the application.", "", Alert.AlertType.INFORMATION);
            } else {
                PopUp.showPopUpMessage("Registration Error", "The username is already taken.", "Select another.", Alert.AlertType.ERROR);
            }
        });
    }

    private void startReceiveThread(){
        Thread thread = NewMain.getReceiveThread();
        thread.setDaemon(true);
        thread.start();
    }

}
