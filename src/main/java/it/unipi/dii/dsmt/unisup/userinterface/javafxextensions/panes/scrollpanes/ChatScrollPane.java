package it.unipi.dii.dsmt.unisup.userinterface.javafxextensions.panes.scrollpanes;

import it.unipi.dii.dsmt.unisup.beans.Chat;
import it.unipi.dii.dsmt.unisup.beans.Message;
import it.unipi.dii.dsmt.unisup.userinterface.javafxextensions.panes.regularpanes.MessagePane;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

import java.util.List;

public class ChatScrollPane extends ScrollPane {
    private static VBox root;

    public ChatScrollPane() {
        relocate(30, 0);
        setPrefSize(550, 530);
        setStyle("-fx-background: #151e24;");

        root = new VBox();
        root.setSpacing(15);
        setContent(root);

        getChildren().add(root);
    }

    public static void addChat(Chat chat) {
        root.getChildren().clear();

        List<Message> messages = chat.getHistory();

        for (Message m: messages) {
            MessagePane chatScrollPane = new MessagePane(m);
            root.getChildren().add(chatScrollPane);
        }
    }
}
