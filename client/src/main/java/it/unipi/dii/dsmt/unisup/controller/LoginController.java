package it.unipi.dii.dsmt.unisup.controller;

import it.unipi.dii.dsmt.unisup.NewMain;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
            try {
                NewMain.getStage().setScene(new Scene(NewMain.loadFXML("MainFrame")));
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        registerBtn.setOnAction(e ->{

        });
    }

}
