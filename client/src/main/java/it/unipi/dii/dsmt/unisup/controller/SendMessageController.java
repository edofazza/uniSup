package it.unipi.dii.dsmt.unisup.controller;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SendMessageController {
    @FXML
    private TextArea messageTextArea;
    @FXML
    private Button sendBtn;
    @FXML
    private TextField usernameField;
    @FXML
    private void initialize() {

        usernameField.setFocusTraversable(true);
        usernameField.requestFocus();
        setActionCommands();
    }

    @FXML
    public void handleCloseButtonAction(ActionEvent event) {
        Stage stage = (Stage) sendBtn.getScene().getWindow();
        stage.close();
    }

    private void setActionCommands() {
        sendBtn.setOnAction(this::handleCloseButtonAction);


    }

}
