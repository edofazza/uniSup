package it.unipi.dii.dsmt.unisup.userinterface.javafxextensions.panes.regularpanes;

import it.unipi.dii.dsmt.unisup.beans.Message;
import it.unipi.dii.dsmt.unisup.communication.MessageGateway;
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
        sendButton.relocate(300, 7);
        sendButton.setStyle("-fx-background-color: white; -fx-border-color: black;");
        sendButton.setOnAction( //TODO
                e -> MessageGateway
                        .getInstance()
                        .sendMessage(
                                new Message("ss", "receiver", messageArea.getText()),
                                5000
                        )
        );

        getChildren().addAll(messageArea);
    }
}
