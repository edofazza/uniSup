package it.unipi.dii.dsmt.unisup.controller;

import it.unipi.dii.dsmt.unisup.NewMain;
import it.unipi.dii.dsmt.unisup.beans.Chat;
import it.unipi.dii.dsmt.unisup.beans.Message;
import it.unipi.dii.dsmt.unisup.communication.MessageGateway;
import it.unipi.dii.dsmt.unisup.utils.Mediator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
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
    private ListView<Chat> messagesList;
    @FXML
    private ListView historyList;
    @FXML
    private TextArea messageTextArea;

    private final int TIMEOUT = 5000;
    private ObservableList<Chat> msgObsList;

    @FXML
    private void initialize() {
        setActionCommands();
        Mediator.setMessagesList(messagesList);
        loadData();
    }
    //example of loading data
    private void loadData() {
        msgObsList = FXCollections.observableArrayList();
        msgObsList.addAll(NewMain.getUserLogged().getChatList());
        messagesList.setItems(msgObsList);
        //TODO fill the message history
        //TODO Save the currently showed chat
    }

    private void setActionCommands() {

        //TODO: missing handler on click on a chat, it should display the messages


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
            String receiverUsername = "GOOFIE"; //TODO take the username of the receiver
            //TODO to get the receiver you need to store the Chat in some way, then you can retrieve the needed data

            String textMessage = messageTextArea.getText();
            Chat chat = new Chat(receiverUsername); //TODO substitute this line with the one to get the correct chat.
            //TODO the chat to use is the one referenced

            if (chat == null || textMessage.equals(""))
                return;

            Message message = new Message(senderUsername, receiverUsername, textMessage);
            MessageGateway
                    .getInstance()
                    .sendMessage(
                            message,
                            TIMEOUT
                    ); //it actually sends the message

            chat.addMessageToHistory(message); //adds the message to the chat model

            messageTextArea.setText("");
            //TODO visualize the new history view of the messages
            //TODO if I'm forgetting something when we send a message to a ALREADY EXISTING CONTACT, please add it. If not, remove this TODO
        });
    }



}
