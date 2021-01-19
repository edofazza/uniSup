package it.unipi.dii.dsmt.unisup.controller;


import it.unipi.dii.dsmt.unisup.NewMain;
import it.unipi.dii.dsmt.unisup.beans.Chat;
import it.unipi.dii.dsmt.unisup.beans.Message;
import it.unipi.dii.dsmt.unisup.communication.MessageGateway;
import it.unipi.dii.dsmt.unisup.userinterface.PopUp;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

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
        sendBtn.setOnAction((e ->{
            if (messageTextArea.getText().equals("")|| usernameField.getText().equals("")) {
                PopUp.showPopUpMessage("Send Error", "All the fields must be filled", "", Alert.AlertType.ERROR);
                return;
            }
            if(NewMain.getUserLogged().userAlreadyPresent(usernameField.getText())) {
                PopUp.showPopUpMessage("Send Error", "The receiver is already present in the chats", "", Alert.AlertType.ERROR);
                return;
            }

            System.out.println(NewMain.getUserLogged().getUsername());

            String senderUsername = NewMain.getUserLogged().getUsername();
            String receiverUsername = usernameField.getText();
            String textMessage = messageTextArea.getText();
            Chat chat = new Chat(receiverUsername);

            Message message = new Message(senderUsername, receiverUsername, textMessage);
            MessageGateway messageGateway = MessageGateway.getInstance();
            messageGateway.sendMessage(message, TIMEOUT);

            chat.addMessageToHistory(message);
            NewMain.getUserLogged().addChat(chat);
            //TODO update ListView of chats
            handleCloseButtonAction(new ActionEvent());
        }));

    }

    /*
    private void sendMessage(ActionEvent e) {
        //check if the usernameField is empty
        String username = usernameField.getText();
        if(!username.isEmpty() || !username.trim().isEmpty()){
            //check if the inserted username is already in the list
            Chat existed = messagesList.getItems().stream().filter
                    (x->username.equalsIgnoreCase(x.getUsernameContact())).findAny().orElse(null);
            System.out.println(existed);
            if (existed == null){
                existed = new Chat(username);
                messagesList.getItems().add(0, existed);
            }
            existed.addMessageToHistory(new Message("io", username, messageTextArea.getText()));
            //close the popup
           handleCloseButtonAction(e);
        }
    }*/

}
