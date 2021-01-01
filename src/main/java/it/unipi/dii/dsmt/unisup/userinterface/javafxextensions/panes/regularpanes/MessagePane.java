package it.unipi.dii.dsmt.unisup.userinterface.javafxextensions.panes.regularpanes;

import it.unipi.dii.dsmt.unisup.beans.Message;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class MessagePane extends Pane {
    private Message message;
    private VBox root;

    public MessagePane(Message message) {
        this.message = message;


        // FORMAT AND INSERT TEXT
        String text = message.getText();
        int size = text.length() - 1;



        String formattedText;

        List<Label> labels = new ArrayList<>();
        for (int i = 0; size > 0 ; size -= 48, i++) {
            System.out.println(size);
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
            if (message.getSender() == "me") { // TODO
                root.setStyle("-fx-background-color: #282e32;");
            } else {
                root.setStyle("-fx-background-color: #074740;");
                root.setLayoutX(200);
            }
            l.setStyle("-fx-text-fill: white; -fx-font-size: 14;");
            root.getChildren().add(l);
        }

        getChildren().add(root);
    }
}
