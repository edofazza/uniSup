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

    private final double X_POS = 0;
    private final double Y_POS = 530;
    private final String STYLE = "-fx-border-color: grey transparent transparent transparent; -fx-border-width: 5px;";

    private final double TEXTAREA_WIDTH = 450;
    private final double TEXTAREA_HEIGHT = 80;
    private final double TEXTAREA_X_POS = 50;
    private final double TEXTAREA_Y_POS = 25;

    private final String POST_BUTTON_TEXT = "POST";
    private final double POST_BUTTON_X_POS = 515;
    private final double POST_BUTTON_Y_POS = 50;
    private final String POST_BUTTON_STYLE = "-fx-background-color: white; -fx-border-color: black;";

    private final int TIMEOUT = 5000;

    public InsertMessagePane() {
        relocate(X_POS, Y_POS);
        setStyle(STYLE);

        messageArea = new TextArea();
        messageArea.setPrefSize(TEXTAREA_WIDTH, TEXTAREA_HEIGHT);
        messageArea.relocate(TEXTAREA_X_POS, TEXTAREA_Y_POS);

        Button sendButton = new Button(POST_BUTTON_TEXT);
        sendButton.relocate(POST_BUTTON_X_POS, POST_BUTTON_Y_POS);
        sendButton.setStyle(POST_BUTTON_STYLE);
        sendButton.setOnAction(
                e -> sendButtonAction()
        );

        getChildren().addAll(messageArea, sendButton);
    }

    private void sendButtonAction() {
        if (messageArea.getText().equals(""))
            return;

        Chat chat = ChatScrollPane.getChat();

        if (chat == null)
            return;

        Message message = new Message(CurrentUI.getUser().getUsername(), chat.getUsernameContact(), messageArea.getText());
        MessageGateway
                .getInstance()
                .sendMessage(
                        message,
                        TIMEOUT
                );

        chat.addMessageToHistory(message);

        messageArea.setText("");
        ChatScrollPane.addChat(chat);
    }
}
