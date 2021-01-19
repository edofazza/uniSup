package it.unipi.dii.dsmt.unisup.userinterface.javafxextensions.panes.regularpanes;

import it.unipi.dii.dsmt.unisup.beans.Message;
import it.unipi.dii.dsmt.unisup.userinterface.CurrentUI;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import java.util.ArrayList;
import java.util.List;

public class MessagePane extends Pane {
    private Message message;
    private VBox root;

    private final String TEXT_STYLE = "-fx-text-fill: white; -fx-font-size: 14;";
    private final String RECEIVED_MSG_STYLE = "-fx-background-color: #282e32;";
    private final String SENT_MSG_STYLE = "-fx-background-color: #074740;";

    public MessagePane(Message message) {
        this.message = message;

        // FORMAT AND INSERT TEXT
        String text = message.getText();
        int size = text.length();

        String formattedText;

        List<Label> labels = new ArrayList<>();
        for (int i = 0; size > 0 ; size -= 48, i++) {
            if (50 < size) {
                formattedText = text.substring(48 * i, 48 + 48 * i);
                Label labelText = new Label(formattedText);
                labels.add(labelText);
            }
            else {
                formattedText = text.substring(48 * i, 48 * i + size);
                Label labelText = new Label(formattedText);
                labels.add(labelText);
                break;
            }
        }

        root = new VBox();
        for (Label l: labels) {// check if the message is sent by me or not
            l.setStyle(TEXT_STYLE);
            root.getChildren().add(l);
        }

        if (message.getReceiver().equals(CurrentUI.getUser().getUsername())) {
            root.setStyle(RECEIVED_MSG_STYLE);
        } else {
            root.setStyle(SENT_MSG_STYLE);
            root.setLayoutX(200);
        }

        getChildren().add(root);
    }
}
