package it.unipi.dii.dsmt.unisup;

import it.unipi.dii.dsmt.unisup.beans.Chat;
import it.unipi.dii.dsmt.unisup.beans.Message;
import it.unipi.dii.dsmt.unisup.beans.User;
import it.unipi.dii.dsmt.unisup.communication.AuthGateway;
import it.unipi.dii.dsmt.unisup.communication.Authenticator;
import it.unipi.dii.dsmt.unisup.communication.MessageGateway;
import it.unipi.dii.dsmt.unisup.utils.ChatSorter;
import it.unipi.dii.dsmt.unisup.utils.Mediator;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;

public class Main extends Application {
    private static Stage guiStage;
    private static User userLogged;
    private static Thread receiveThread;

    private static final Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    public final static int DEF_FRAME_WIDTH = 950;
    public final static int DEF_FRAME_HEIGHT = 600;
    public final static int LOG_FRAME_WIDTH = 420;
    public final static int LOG_FRAME_HEIGHT = 522;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        guiStage = stage;
        stage.setResizable(false);
        stage.setTitle("UniSup");
        stage.getIcons().add(new javafx.scene.image.Image("/images/logo.png"));
        Scene scene = new Scene(loadFXML("LoginFrame"));
        stage.setScene(scene);
        stage.setOnCloseRequest(e-> userExit());
        stage.show();
    }

    public static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/"+ fxml + ".fxml"));

        if(fxml.equalsIgnoreCase("LoginFrame")){
            guiStage.setX(dim.width/2 - LOG_FRAME_WIDTH / 2);
            guiStage.setY(dim.height/2 - LOG_FRAME_HEIGHT / 2);
        }else {
            guiStage.setX(dim.width / 2 - DEF_FRAME_WIDTH / 2);
            guiStage.setY(dim.height / 2 - DEF_FRAME_HEIGHT / 2);
        }
        return fxmlLoader.load();
    }

    public static Stage getStage() {
        return guiStage;
    }

    public static void changeStage(String sceneToLoad) {
        try {
            if (userLogged != null) {
                guiStage.setTitle("UniSup -  " + userLogged.getUsername());
                guiStage.setResizable(true);
            }else {
                guiStage.setTitle("UniSup");
                guiStage.setResizable(false);
            }
            guiStage.setScene(new Scene(Main.loadFXML(sceneToLoad)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setUser(User user) {
        userLogged = user;
    }

    public static User getUserLogged() {
        return userLogged;
    }

    public static void userExit() {
        if(getUserLogged()!=null) {
            Authenticator au = AuthGateway.getInstance();
            au.logout(getUserLogged()); //logs out the user sending a stop consumer request
            userLogged = null;
        }
        if(receiveThread!=null) {
            receiveThread.interrupt();
            receiveThread = null;
        }
    }

    public static Thread getReceiveThread(){
        if(receiveThread==null){
            receiveThread = new Thread(new ListenerTask());
        }
        return receiveThread;
    }

    static class ListenerTask implements Runnable {

        @Override
        public void run() {
            Runnable updater;

            while (true) {
                MessageGateway messageGateway = MessageGateway.getInstance();
                Message m = messageGateway.receiveMessage();

                User userLogged = Main.getUserLogged();
                // INSERT IT INTO USER
                if (userLogged == null)
                    return;


                Chat modifiedChat = userLogged.insertMessage(m); //also updates the Chat model
                modifiedChat.increaseUnreadMessages(1);
                updater = new ListenerTaskJavaFx(m, modifiedChat);
                Platform.runLater(updater);
            }
        }
    }


    static class ListenerTaskJavaFx implements Runnable {
        private final Message receivedMsg;
        private final Chat modifiedChat;

        ListenerTaskJavaFx(Message receivedMsg, Chat modifiedChat){
            this.receivedMsg=receivedMsg;
            this.modifiedChat = modifiedChat;
        }

        @Override
        public void run(){
            //Maybe later, we will implement a notification mechanism for messages received in other chats different from the one opened
            updateContactListView();
        }

        private void updateContactListView(){
            ListView<Chat> contactList = Mediator.getContactList();
            ObservableList<Chat> contactObsList = Mediator.getContactObsList();
            ObservableList<Message> histObsList = Mediator.getHistObsList();
            ListView<Message> historyList = Mediator.getHistoryList();
            Chat selected = contactList.getSelectionModel().getSelectedItem();
            contactObsList.clear();
            contactObsList.addAll(new ChatSorter(Main.getUserLogged().getChatList()).sort());
            contactList.setItems(contactObsList);
            if (selected != null){
                if (selected.getUsernameContact().equals(modifiedChat.getUsernameContact())) {
                    histObsList.clear();
                    histObsList.addAll(modifiedChat.getHistory());
                    historyList.setItems(histObsList);
                    historyList.scrollTo(histObsList.get(histObsList.size() - 1));
                    contactList.getSelectionModel().select(0);
                    contactList.getFocusModel().focus(0);
                }else{
                    int index = contactObsList.indexOf(selected);
                    contactList.getSelectionModel().select(selected);
                    contactList.getFocusModel().focus(index);
                }
            }
        }


    }
}
