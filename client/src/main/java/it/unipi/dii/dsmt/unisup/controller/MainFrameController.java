package it.unipi.dii.dsmt.unisup.controller;

import it.unipi.dii.dsmt.unisup.NewMain;
import it.unipi.dii.dsmt.unisup.beans.Chat;
import it.unipi.dii.dsmt.unisup.beans.Message;
import it.unipi.dii.dsmt.unisup.communication.MessageGateway;
import it.unipi.dii.dsmt.unisup.utils.Mediator;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
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
    @FXML
    private Label usernameLbl;

    private final int TIMEOUT = 5000;
    private ObservableList<Chat> contactObsList;
    private ObservableList<Message> histObsList;
    private Chat selectedChat;

    @FXML
    private void initialize() {
        contactList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        usernameLbl.setText(NewMain.getUserLogged().getUsername());
        setActionCommands();
        loadData();
        Mediator.setContactList(contactList);
        Mediator.setContactObsList(contactObsList);
        Mediator.setHistoryList(historyList);
        Mediator.setHistObsList(histObsList);
    }
    //example of loading data
    private void loadData() {
        contactObsList = FXCollections.observableArrayList();
        contactObsList.addAll(NewMain.getUserLogged().getChatList());
        contactList.setItems(contactObsList);

        histObsList = FXCollections.observableArrayList();
        //TODO sort the contact list according to their message history timestamp
        //by default the most current is selected
        if (contactObsList.size() > 0) {
            selectedChat = contactObsList.get(0);
            Mediator.setSelectedChat(selectedChat);
            histObsList.addAll(selectedChat.getHistory());
        }
        historyList.setItems(histObsList);
    }

    private void setActionCommands() {

        contactList.setOnMouseClicked(e ->{
            if (contactList.getItems().size() == 0) return;
            selectedChat = contactList.getSelectionModel().getSelectedItem();
            Mediator.setSelectedChat(selectedChat);
            updateMessageHistoryView();
        });

        logoutBtn.setOnAction(e ->{
            NewMain.userExit();
            NewMain.changeStage("LoginFrame");
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


        sendBtn.setOnAction(e ->{
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

            messageTextArea.setText("");
            updateMessageHistoryView();
        });
    }

    private void updateMessageHistoryView() {
        //TODO make the string more beautiful --> toString method in Message
        Platform.runLater(()->{
            histObsList.add(selectedChat.getHistory().get(selectedChat.getHistory().size() - 1));
        });
    }

    private void updateContactListView(){
        Platform.runLater(()->{
            contactObsList.clear();
            contactObsList.addAll(NewMain.getUserLogged().getChatList());
            contactList.setItems(contactObsList);
        });
    }


}
