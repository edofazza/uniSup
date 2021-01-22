package it.unipi.dii.dsmt.unisup.controller;

import it.unipi.dii.dsmt.unisup.NewMain;
import it.unipi.dii.dsmt.unisup.beans.Chat;
import it.unipi.dii.dsmt.unisup.beans.Message;
import it.unipi.dii.dsmt.unisup.communication.MessageGateway;
import it.unipi.dii.dsmt.unisup.userinterface.ContactCell;
import it.unipi.dii.dsmt.unisup.userinterface.MessageCell;
import it.unipi.dii.dsmt.unisup.utils.ChatSorter;
import it.unipi.dii.dsmt.unisup.utils.LastMessageTracker;
import it.unipi.dii.dsmt.unisup.utils.Mediator;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;


public class MainFrameController {
    @FXML
    private Button sendBtn;
    @FXML
    private Button newMessageBtn;
    @FXML
    private Button logoutBtn;
    @FXML
    private ListView<Chat> contactList;
    @FXML
    private ListView<Message> historyList;
    @FXML
    private TextArea messageTextArea;

    private final int TIMEOUT = 5000;
    private ObservableList<Chat> contactObsList;
    private ObservableList<Message> histObsList;
    private Chat selectedChat;

    @FXML
    private void initialize() {
        contactList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        loadData();
        setActionCommands();
        Mediator.setContactList(contactList);
        Mediator.setContactObsList(contactObsList);
        Mediator.setHistoryList(historyList);
        Mediator.setHistObsList(histObsList);
    }
    //example of loading data
    private void loadData() {
        contactObsList = FXCollections.observableArrayList();
        contactObsList.addAll(new ChatSorter(NewMain.getUserLogged().getChatList()).sort());
        contactList.setItems(contactObsList);
        histObsList = FXCollections.observableArrayList();
        historyList.setItems(histObsList);
    }

    private void setActionCommands() {

        contactList.setOnMouseClicked(e ->{
            if (contactList.getItems().size() == 0 || contactList.getSelectionModel().getSelectedItem() == null) return;
            if (sendBtn.isDisabled()) {
                sendBtn.setDisable(false);
                messageTextArea.setDisable(false);
            }

            selectedChat = contactList.getSelectionModel().getSelectedItem();
            if (selectedChat.hasUnreadMessages()) {
                selectedChat.readAllMessages();
                updateContactListView();
            }
            LastMessageTracker.setLastTimestamp(selectedChat);
            updateAllMessageHistoryView();
        });

        logoutBtn.setOnAction(e ->{
            NewMain.userExit();
            NewMain.changeStage("LoginFrame");
        });
        historyList.setCellFactory(param -> new MessageCell());
        contactList.setCellFactory(param -> new ContactCell());

        sendBtn.setOnAction(e ->{
            selectedChat = contactList.getSelectionModel().getSelectedItem();
            String senderUsername = NewMain.getUserLogged().getUsername();
            String receiverUsername = selectedChat.getUsernameContact();
            String textMessage = messageTextArea.getText();

            if (textMessage.isEmpty() || textMessage.trim().isEmpty()) return;

            Message message = new Message(senderUsername, receiverUsername, textMessage);
            MessageGateway
                    .getInstance()
                    .sendMessage(
                            message,
                            TIMEOUT
                    ); //it actually sends the message

            selectedChat.addMessageToHistory(message); //adds the message to the chat model

            messageTextArea.clear();
            updateAllContactListView();
            updateLastMessageHistoryView(message);

        });
        newMessageBtn.setOnAction(event ->{
            final Stage dialog;
            try {
                dialog = new Stage();
                dialog.setResizable(false);
                dialog.setTitle("UniSup");
                dialog.getIcons().add(new javafx.scene.image.Image("/images/logo.png"));
                dialog.initModality(Modality.APPLICATION_MODAL);
                dialog.initOwner(NewMain.getStage());
                Scene dialogScene = new Scene(NewMain.loadFXML("SendMessage"));
                dialog.setScene(dialogScene);
                dialog.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        messageTextArea.setDisable(true);
        sendBtn.setDisable(true);
    }
    private void updateAllMessageHistoryView(){
        Platform.runLater(()->{
            histObsList.clear();
            histObsList.addAll(selectedChat.getHistory());
            historyList.setItems(histObsList);
            historyList.scrollTo(histObsList.get(histObsList.size() - 1));
        });
    }
    private void updateLastMessageHistoryView(Message message) {
        Platform.runLater(()->{
            histObsList.add(message);
            historyList.scrollTo(message);
        });
    }
    private void updateContactListView(){
        int index = contactObsList.indexOf(selectedChat);
        contactObsList.remove(selectedChat);
        contactObsList.add(index, selectedChat);
        contactList.getSelectionModel().select(index);
        contactList.getFocusModel().focus(index);
    }
    private void updateAllContactListView(){
        contactObsList.clear();
        contactObsList.addAll(new ChatSorter(NewMain.getUserLogged().getChatList()).sort());
        contactList.setItems(contactObsList);
        contactList.getSelectionModel().selectFirst();
        contactList.getFocusModel().focus(0);
    }

}
