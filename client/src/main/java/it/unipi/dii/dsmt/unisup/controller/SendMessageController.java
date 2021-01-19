package it.unipi.dii.dsmt.unisup.controller;


import it.unipi.dii.dsmt.unisup.beans.Chat;
import it.unipi.dii.dsmt.unisup.beans.Message;
import it.unipi.dii.dsmt.unisup.communication.MessageGateway;
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
        //TODO if Mirco(me) understood well, here we only manage the popup when we click on NEW MESSAGE
        //if not, please add what is needed
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

            //TODO check that the receiver is not on the contact list. User.java and Chat.java have awesome methods to perform this check

            Chat chat = new Chat(receiverUsername); //The chat with this contact should be empty

            Message message = new Message(senderUsername, receiverUsername, textMessage);
            MessageGateway
                    .getInstance()
                    .sendMessage(
                            message,
                            TIMEOUT
                    );

            chat.addMessageToHistory(message);
            //TODO update ListView of chats
            //TODO add the new chat on the chat list of the user object
            //TODO if I'm forgetting something when we send a message to a NEW CONTACT, please add it. If not, remove this TODO
            handleCloseButtonAction(new ActionEvent());
        }));

    }

}
