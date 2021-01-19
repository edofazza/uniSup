package it.unipi.dii.dsmt.unisup.controller;


import it.unipi.dii.dsmt.unisup.NewMain;
import it.unipi.dii.dsmt.unisup.beans.Chat;
import it.unipi.dii.dsmt.unisup.beans.Message;
import it.unipi.dii.dsmt.unisup.communication.MessageGateway;
import it.unipi.dii.dsmt.unisup.userinterface.CurrentUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.ArrayList;

public class SendMessageController {
    @FXML
    private TextArea messageTextArea;
    @FXML
    private Button sendBtn;
    @FXML
    private TextField usernameField;

    private final int TIMEOUT = 5000;


    @FXML
    private void initialize() {
        //TODO add a close button and connect it with handle close button action
        usernameField.setFocusTraversable(true);
        usernameField.requestFocus();
        setActionCommands();
    }

    @FXML
    public void handleCloseButtonAction(ActionEvent event) {
        //TODO empty TextField and TextArea
        Stage stage = (Stage) sendBtn.getScene().getWindow();
        stage.close();
    }

    private void setActionCommands() {
        sendBtn.setOnAction((e ->{
            if (messageTextArea.getText().equals("") || CurrentUI.getUser().userAlreadyPresent(usernameField.getText()))
                return;

            String senderUsername = NewMain.getUserLogged().getUsername();
            String receiverUsername = usernameField.getText();
            String textMessage = messageTextArea.getText();
            Chat chat = new Chat(receiverUsername, new ArrayList<>());

            Message message = new Message(senderUsername, receiverUsername, textMessage);
            MessageGateway
                    .getInstance()
                    .sendMessage(
                            message,
                            TIMEOUT
                    );

            chat.addMessageToHistory(message);
            NewMain.getUserLogged().addChat(chat);

            handleCloseButtonAction(new ActionEvent());
        }));

    }

}
