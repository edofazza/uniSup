package it.unipi.dii.dsmt.unisup.controller;


import it.unipi.dii.dsmt.unisup.Main;
import it.unipi.dii.dsmt.unisup.beans.Chat;
import it.unipi.dii.dsmt.unisup.beans.Message;
import it.unipi.dii.dsmt.unisup.communication.MessageGateway;
import it.unipi.dii.dsmt.unisup.userinterface.PopUp;
import it.unipi.dii.dsmt.unisup.utils.ChatSorter;
import it.unipi.dii.dsmt.unisup.utils.LastMessageTracker;
import it.unipi.dii.dsmt.unisup.utils.Mediator;
import javafx.application.Platform;
import javafx.collections.ObservableList;
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

            if(Main.getUserLogged().userAlreadyPresent(usernameField.getText())) {
                PopUp.showPopUpMessage("Send Error", "The receiver is already present in the chats", "", Alert.AlertType.ERROR);
                return;
            }

            if(Main.getUserLogged().getUsername().equals(usernameField.getText())) {
                PopUp.showPopUpMessage("Send Error", "You cannot send messages to yourself", "", Alert.AlertType.ERROR);
                return;
            }

            System.out.println(Main.getUserLogged().getUsername());

            String senderUsername = Main.getUserLogged().getUsername();
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
            Main.getUserLogged().addChat(chat);
            LastMessageTracker.setLastTimestamp(chat);
            handleCloseButtonAction(new ActionEvent());
            //
            updateContactListView(message);
            //

            if (Mediator.getMainFrameSendButton().isDisabled()) {
                Mediator.getMainFrameSendButton().setDisable(false);
                Mediator.getMainFrameMsgTextArea().setDisable(false);
            }
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
            contactObsList.addAll(new ChatSorter(Main.getUserLogged().getChatList()).sort());
            contactList.setItems(contactObsList);
            contactList.getSelectionModel().selectFirst();
            contactList.getFocusModel().focus(0);
            histObsList.clear();
            histObsList.addAll(contactList.getSelectionModel().getSelectedItem().getHistory());
            historyList.setItems(histObsList);
        });
    }

}
