package it.unipi.dii.dsmt.unisup.controller;

import it.unipi.dii.dsmt.unisup.NewMain;
import it.unipi.dii.dsmt.unisup.beans.Chat;
import it.unipi.dii.dsmt.unisup.beans.Message;
import it.unipi.dii.dsmt.unisup.beans.User;
import it.unipi.dii.dsmt.unisup.communication.AuthGateway;
import it.unipi.dii.dsmt.unisup.communication.Authenticator;
import it.unipi.dii.dsmt.unisup.communication.MessageGateway;
import it.unipi.dii.dsmt.unisup.userinterface.CurrentUI;
import it.unipi.dii.dsmt.unisup.userinterface.javafxextensions.panes.scrollpanes.ChatScrollPane;
import it.unipi.dii.dsmt.unisup.userinterface.javafxextensions.panes.scrollpanes.ContactUserPanes;
import javafx.application.Platform;
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
    private ListView messagesList;
    @FXML
    private ListView historyList;
    @FXML
    private TextArea messageTextArea;

    private final int TIMEOUT = 5000;


    @FXML
    private void initialize() {
        setActionCommands();
        //TODO load chats and messages
        startReceiveThread();
    }

    private void setActionCommands() {
        logoutBtn.setOnAction(e ->{
            Authenticator au = AuthGateway.getInstance();
            au.logout(CurrentUI.getUser());
            CurrentUI.userExit();

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
            Chat chat = ChatScrollPane.getChat(); //TODO get the actual chat

            if (chat == null || messageTextArea.getText().equals(""))
                return;

            String senderUsername = NewMain.getUserLogged().getUsername();
            String receiverUsername = "GOOFIE"; //TODO take the username of the receiver (from the chat currently selected)
            String textMessage = messageTextArea.getText();

            Message message = new Message(senderUsername, receiverUsername, textMessage);
            MessageGateway
                    .getInstance()
                    .sendMessage(
                            message,
                            TIMEOUT
                    );

            chat.addMessageToHistory(message);

            messageTextArea.setText("");

            //TODO visualize the new chat (Mirco)
            // (by Edoardo) I think it's automatically done by the ListView
        });
    }

    private void startReceiveThread(){
        Thread thread = new Thread(new MainFrameController.ListenerTask());
        thread.setDaemon(true);
        thread.start();
    }

    static class ListenerTask implements Runnable {

        @Override
        public void run() {
            Runnable updater = new MainFrameController.ListenerTaskJavaFx();

            while (true) {
                MessageGateway messageGateway = MessageGateway.getInstance();
                Message m = messageGateway.receiveMessage();

                User userLogged = NewMain.getUserLogged();
                // INSERT IT INTO USER
                if (userLogged == null)
                    return;

                userLogged.insertMessage(m);
                Platform.runLater(updater);
            }
        }
    }


    static class ListenerTaskJavaFx implements Runnable {

        @Override
        public void run(){
            ContactUserPanes.insertContacts(); //TODO update the contact list on the left of the UI
            ChatScrollPane.addChat(ChatScrollPane.getChat()); //TODO update chats and refresh the display
        }
    }
}
