package it.unipi.dii.dsmt.unisup.userinterface.javafxextensions.panes.scrollpanes;

import it.unipi.dii.dsmt.unisup.beans.Chat;
import it.unipi.dii.dsmt.unisup.beans.Message;
import it.unipi.dii.dsmt.unisup.userinterface.javafxextensions.panes.regularpanes.MessagePane;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

import java.util.List;

public class ChatScrollPane extends ScrollPane {
    private static VBox root;
    private static Chat chat;

    private final int X_POS = 30;
    private final int Y_POS = 0;
    private final int WIDTH = 550;
    private final int HEIGHT = 530;
    private final String STYLE = "-fx-background: #151e24;";
    private final double SPACING = 15;

    public ChatScrollPane() {
        relocate(X_POS, Y_POS);
        setPrefSize(WIDTH, HEIGHT);
        setStyle(STYLE);

        root = new VBox();
        root.setSpacing(SPACING);
        setContent(root);

        getChildren().add(root);
    }

    public static void addChat(Chat c) {
        if (c == null)
            return;

        root.getChildren().clear();
        chat = c;

        List<Message> messages = chat.getHistory();

        for (Message m: messages) {
            MessagePane chatScrollPane = new MessagePane(m);
            root.getChildren().add(chatScrollPane);
        }
    }

    public static Chat getChat() {
        return chat;
    }
}
