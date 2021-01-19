package it.unipi.dii.dsmt.unisup.controller;


import it.unipi.dii.dsmt.unisup.beans.Chat;
import it.unipi.dii.dsmt.unisup.beans.Message;
import it.unipi.dii.dsmt.unisup.utils.Mediator;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.function.Predicate;

public class SendMessageController {
    @FXML
    private TextArea messageTextArea;
    @FXML
    private Button sendBtn;
    @FXML
    private TextField usernameField;

    private ListView<Chat> messagesList;
    @FXML
    private void initialize() {

        usernameField.setFocusTraversable(true);
        usernameField.requestFocus();
        setActionCommands();
        messagesList = Mediator.getMessagesList();
    }

    @FXML
    public void handleCloseButtonAction(ActionEvent event) {
        Stage stage = (Stage) sendBtn.getScene().getWindow();
        stage.close();
    }

    private void setActionCommands() {
       sendBtn.setOnAction(this::sendMessage);


    }

    private void sendMessage(ActionEvent e) {
        //check if the usernameField is empty
        String username = usernameField.getText();
        if(!username.isEmpty() || !username.trim().isEmpty()){
            //check if the inserted username is already in the list
            Chat existed = messagesList.getItems().stream().filter
                    (x->username.equalsIgnoreCase(x.getUsernameContact())).findAny().orElse(null);
            System.out.println(existed);
            if (existed == null){
                //TODO: add it into the db
                existed = new Chat(username);
                messagesList.getItems().add(0, existed);
            }
            existed.addMessageToHistory(new Message("io", username, messageTextArea.getText()));
            //close the popup
           handleCloseButtonAction(e);
        }
    }

}
