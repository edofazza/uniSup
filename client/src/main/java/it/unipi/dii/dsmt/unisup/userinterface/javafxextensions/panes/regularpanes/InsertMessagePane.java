package it.unipi.dii.dsmt.unisup.userinterface.javafxextensions.panes.regularpanes;

import it.unipi.dii.dsmt.unisup.beans.Chat;
import it.unipi.dii.dsmt.unisup.beans.Message;
import it.unipi.dii.dsmt.unisup.communication.MessageGateway;
import it.unipi.dii.dsmt.unisup.userinterface.CurrentUI;
import it.unipi.dii.dsmt.unisup.userinterface.javafxextensions.panes.scrollpanes.ChatScrollPane;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;

public class InsertMessagePane extends Pane {
    private TextArea messageArea;

    public InsertMessagePane() {
        relocate(0, 530);
        setStyle("-fx-border-color: grey transparent transparent transparent; -fx-border-width: 5px;");

        messageArea = new TextArea();
        messageArea.setPrefSize(450, 80);
        messageArea.relocate(50, 25);

        Button sendButton = new Button("POST");
        sendButton.relocate(515, 50);
        sendButton.setStyle("-fx-background-color: white; -fx-border-color: black;");
        sendButton.setOnAction( //TODO
                e -> sendButtonAction()
        );

        getChildren().addAll(messageArea, sendButton);
    }

    private void sendButtonAction() {
        Chat chat = ChatScrollPane.getChat();

        if (chat == null)
            return;

        Message message = new Message(CurrentUI.getUser().getUsername(), chat.getUsernameContact(), messageArea.getText());
        MessageGateway
                .getInstance()
                .sendMessage(
                        message,
                        5000
                );

        chat.addMessageToHistory(message);

        ChatScrollPane.addChat(chat);
    }
}
