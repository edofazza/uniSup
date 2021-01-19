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

        // TODO load chats and messages
        // TODO ANSWER (by Edoardo): Already done in the LoginController when the user logs in

        Mediator.setMessagesList(this.messagesList);
        //TODO: the following method is imp to load data (sina), I know there is a method to load data
        //TODO: but I dont know how to use it. we have a object called msgObsList which is the model of the view
        //TODO: I add all the data into it.
        loadData();
    }
    //example of loading data
    private void loadData() {
        msgObsList = FXCollections.observableArrayList();
        msgObsList.addAll(NewMain.getUserLogged().getChatList());
        messagesList.setItems(msgObsList);
    }

    private void setActionCommands() {
        logoutBtn.setOnAction(e ->{
            NewMain.userExit(); //TODO here remove the user object from the data structure that holds the current logged user

            NewMain.changeStage("LoginFrame");

            //TODO if I'm forgetting something at logout time, please add it. If not, remove this TODO
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
            //TODO if Mirco(me) understood well, there is no business logic here. If not, please add it or tell me
        });


        sendBtn.setOnAction(e ->{
            String senderUsername = NewMain.getUserLogged().getUsername();
            String receiverUsername = "GOOFIE"; //TODO take the username of the receiver
            // TODO to get the receiver you need to store the Chat in some way, then you can retrieve the needed data

            String textMessage = messageTextArea.getText(); //TODO take the message text from the text area
            Chat chat = new Chat(receiverUsername); //TODO substitute this line with the one to get the correct chat.
                                                    //Chat.java and User.java have useful methods to do it

            //TODO there should be impossible to send a message here in a non-existing chat,
            //so, if needed, substitute the following 2 lines with the correct control
            if (chat == null)
                return;

            Message message = new Message(senderUsername, receiverUsername, textMessage);
            MessageGateway
                    .getInstance()
                    .sendMessage(
                            message,
                            TIMEOUT
                    ); //it actually sends the message

            chat.addMessageToHistory(message); //adds the message to the chat model

            //TODO empty text area
            messageTextArea.setText("");
            //TODO visualize the new chat
            //TODO if I'm forgetting something when we send a message to a ALREADY EXISTING CONTACT, please add it. If not, remove this TODO
        });
    }



}
