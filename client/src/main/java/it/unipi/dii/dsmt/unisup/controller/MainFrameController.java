package it.unipi.dii.dsmt.unisup.controller;

import it.unipi.dii.dsmt.unisup.NewMain;
import it.unipi.dii.dsmt.unisup.beans.Chat;
import it.unipi.dii.dsmt.unisup.beans.Message;
import it.unipi.dii.dsmt.unisup.utils.Mediator;
import it.unipi.dii.dsmt.unisup.beans.Chat;
import it.unipi.dii.dsmt.unisup.beans.Message;
import it.unipi.dii.dsmt.unisup.communication.AuthGateway;
import it.unipi.dii.dsmt.unisup.communication.Authenticator;
import it.unipi.dii.dsmt.unisup.communication.MessageGateway;
import it.unipi.dii.dsmt.unisup.userinterface.CurrentUI;
import it.unipi.dii.dsmt.unisup.userinterface.javafxextensions.panes.scrollpanes.ChatScrollPane;
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


    @FXML
    private void initialize() {
        setActionCommands();
        Mediator.setMessagesList(this.messagesList);
    }

    private void setActionCommands() {
        logoutBtn.setOnAction(e ->{
            Authenticator au = AuthGateway.getInstance();
            au.logout(CurrentUI.getUser());
            CurrentUI.userExit();
                try {
                    NewMain.getStage().setScene(new Scene(NewMain.loadFXML("LoginFrame")));
                } catch (IOException ioException) {
                ioException.printStackTrace();
            }
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
            String senderUsername = "GOOFIE"; //TODO take the username of the sender (CurrentUI can help you)
            String receiverUsername = "GOOFIE"; //TODO take the username of the receiver
            String textMessage = "GOOFIE"; //TODO take the message text from the text area
            Chat chat = ChatScrollPane.getChat(); //TODO get the actual chat

            if (chat == null)
                return;

        });
    }
}
