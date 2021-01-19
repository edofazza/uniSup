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
            User user = new User(username, password); //We instantiate a new User Object for the current user
            Authenticator au = AuthGateway.getInstance();
            if(au.login(user)) {     //Tries to login
                CurrentUI.setUser(user); //TODO set the logged user as current user in a data structure (better if static)

                // LOAD THE CHATS
                AuthGateway authGateway = AuthGateway.getInstance();
                user.setChatList(authGateway.getChatHistory(user)); //Retrieves chat history of the logged user and add them to the chat list.
                                                                    //So, every time you need them, pick them from the user object
                //TODO if I'm forgetting something at login time, please add it. If not, remove this TODO
                try {
                    NewMain.getStage().setScene(new Scene(NewMain.loadFXML("MainFrame")));
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
            else{
                //TODO display the error label (authentication failed)
            }
        });
        registerBtn.setOnAction(e ->{
            String username = "GOOFIE"; //TODO take the actual username from the TextField
            String password = "GOOFIE"; //TODO take the actual username from the TextField
            Authenticator auth = AuthGateway.getInstance();
            User user = new User(username,password);
            boolean result = auth.register(user);

            //TODO if I'm forgetting something at registration time, please add it. If not, remove this TODO

            if (result) {
                //TODO display success label
            } else {
                //TODO display error label
            }
        });
    }

}
