package it.unipi.dii.dsmt.unisup.controller;


import it.unipi.dii.dsmt.unisup.beans.Chat;
import it.unipi.dii.dsmt.unisup.beans.Message;
import it.unipi.dii.dsmt.unisup.communication.MessageGateway;
import it.unipi.dii.dsmt.unisup.userinterface.javafxextensions.panes.scrollpanes.ChatScrollPane;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
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
            String senderUsername = "GOOFIE"; //TODO take the username of the sender (CurrentUI can help you)
            String receiverUsername = "GOOFIE"; //TODO take the username of the receiver
            String textMessage = "GOOFIE"; //TODO take the message text from the text area
            Chat chat = ChatScrollPane.getChat(); //TODO get the actual chat

            //TODO check that the receiver is not on the contact list

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

}
