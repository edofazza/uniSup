package it.unipi.dii.dsmt.unisup.controller;


import it.unipi.dii.dsmt.unisup.beans.Chat;
import it.unipi.dii.dsmt.unisup.beans.Message;
import it.unipi.dii.dsmt.unisup.utils.Mediator;
import it.unipi.dii.dsmt.unisup.beans.Chat;
import it.unipi.dii.dsmt.unisup.beans.Message;
import it.unipi.dii.dsmt.unisup.communication.MessageGateway;
import it.unipi.dii.dsmt.unisup.userinterface.javafxextensions.panes.scrollpanes.ChatScrollPane;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.function.Predicate;

public class SendMessageController {
    @FXML
    private TextArea messageTextArea;
    @FXML
    private Button sendBtn;
    @FXML
    private TextField usernameField;

    private final int TIMEOUT = 5000;



    private ListView<Chat> messagesList;
    @FXML
    private void initialize() {
//TODO add a close button and connect it with handle close button action
        usernameField.setFocusTraversable(true);
        usernameField.requestFocus();
        setActionCommands();
        messagesList = Mediator.getMessagesList();
    }

    @FXML
    public void handleCloseButtonAction(ActionEvent event) {
        //TODO empty TextField and TextArea
        Stage stage = (Stage) sendBtn.getScene().getWindow();
        stage.close();
    }

    private void setActionCommands() {
        sendBtn.setOnAction((e ->{
            String senderUsername = "GOOFIE"; //TODO take the username of the sender (CurrentUI can help you)
            String receiverUsername = "GOOFIE"; //TODO take the username of the receiver
            String textMessage = "GOOFIE"; //TODO take the message text from the text area
            Chat chat = ChatScrollPane.getChat(); //TODO get the actual chat

            //TODO check that the receiver is not on the contact list
       sendBtn.setOnAction(this::sendMessage);

            Message message = new Message(senderUsername, receiverUsername, textMessage);
            MessageGateway
                    .getInstance()
                    .sendMessage(
                            message,
                            TIMEOUT
                    );

            chat.addMessageToHistory(message);

            handleCloseButtonAction(new ActionEvent());
        }));

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
