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
import it.unipi.dii.dsmt.unisup.utils.Mediator;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;

import javax.xml.soap.Text;
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
        //TODO load chats and messages
        startReceiveThread();
        Mediator.setMessagesList(this.messagesList);
        //TODO: the following method is imp to load data (sina), I know there is a method to load data
        //TODO: but I dont know how to use it. we have a object called msgObsList which is the model of the view
        //TODO: I add all the data into it.
        loadData();
    }
    //example of loading data
    private void loadData() {
        msgObsList = FXCollections.observableArrayList();
        msgObsList.addAll(CurrentUI.getUser().getChatList());
        messagesList.setItems(msgObsList);
    }

    private void setActionCommands() {
        logoutBtn.setOnAction(e ->{
            Authenticator au = AuthGateway.getInstance();
            au.logout(CurrentUI.getUser()); //logs out the user sending a stop consumer request
            CurrentUI.userExit(); //TODO here remove the user object from the data structure that holds the current logged user
                try {
                    NewMain.getStage().setScene(new Scene(NewMain.loadFXML("LoginFrame")));
                } catch (IOException ioException) {
                ioException.printStackTrace();
            }
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
            String senderUsername = "GOOFIE"; //TODO take the username of the sender (CurrentUI can help you)
            String receiverUsername = "GOOFIE"; //TODO take the username of the receiver
            String textMessage = "GOOFIE"; //TODO take the message text from the text area
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
            //TODO visualize the new chat
            //TODO if I'm forgetting something when we send a message to a ALREADY EXISTING CONTACT, please add it. If not, remove this TODO
        });
    }

    //This is a very important and delicate point of the app. Please modify ONLY on TODOs
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

                User userLogged = CurrentUI.getUser(); //TODO get the actual user from the data structure of the currently logged user
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
            //TODO update the contact list visualization
            //TODO update chats
            //TODO refresh the display, if the message has been received in an open chat, add it
            //TODO if the message has been received in a different chat from the one showed now, update only the chat without visualizing it
            //Maybe later, we will implement a notification mechanism for messages received in other chats different from the one opened
            //TODO if I'm forgetting something here, please CONTACT MIRCO
//            ContactUserPanes.insertContacts(); //TODO update the contact list on the left of the UI
//            ChatScrollPane.addChat(ChatScrollPane.getChat()); //TODO update chats and refresh the display

        }
    }
}
