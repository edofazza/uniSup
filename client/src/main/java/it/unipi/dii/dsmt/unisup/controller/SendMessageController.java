package it.unipi.dii.dsmt.unisup.controller;


import it.unipi.dii.dsmt.unisup.NewMain;
import it.unipi.dii.dsmt.unisup.beans.Chat;
import it.unipi.dii.dsmt.unisup.beans.Message;
import it.unipi.dii.dsmt.unisup.communication.MessageGateway;
import it.unipi.dii.dsmt.unisup.userinterface.PopUp;
import it.unipi.dii.dsmt.unisup.utils.ChatSorter;
import it.unipi.dii.dsmt.unisup.utils.Mediator;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.util.Set;

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
            if (!messageGateway.sendMessage(message, TIMEOUT)) {
                PopUp.showPopUpMessage("Send Error", "The receiver doesn't exist", "", Alert.AlertType.ERROR);
                return;
            }

            chat.addMessageToHistory(message);
            NewMain.getUserLogged().addChat(chat);
            handleCloseButtonAction(new ActionEvent());
            //
            updateContactListView(message);
        }));

    }

    private void updateContactListView(Message message){
        ObservableList<Chat> contactObsList = Mediator.getContactObsList();
        ObservableList<Message> histObsList = Mediator.getHistObsList();
        ListView<Chat> contactList = Mediator.getContactList();
        ListView<Message> historyList = Mediator.getHistoryList();
        Platform.runLater(()->{
            //for sorting the list we need to load the new data
            contactObsList.clear();
            contactObsList.addAll(new ChatSorter(NewMain.getUserLogged().getChatList()).sort());
            contactList.setItems(contactObsList);
            contactList.getSelectionModel().selectFirst();
            contactList.getFocusModel().focus(0);
            histObsList.clear();
            histObsList.addAll(contactList.getSelectionModel().getSelectedItem().getHistory());
            historyList.setItems(histObsList);
        });
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
