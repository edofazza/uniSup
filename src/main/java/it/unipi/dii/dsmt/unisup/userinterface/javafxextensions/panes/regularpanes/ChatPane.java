package it.unipi.dii.dsmt.unisup.userinterface.javafxextensions.panes.regularpanes;

import it.unipi.dii.dsmt.unisup.userinterface.javafxextensions.panes.scrollpanes.ChatScrollPane;
import javafx.scene.layout.Pane;

public class ChatPane extends Pane {
    public ChatPane(int x, int y, int width, int height) {
        relocate(x, y);
        setPrefSize(width, height);

        ChatScrollPane chatScrollPane = new ChatScrollPane();
        InsertMessagePane insertMessagePane = new InsertMessagePane();

        getChildren().addAll(chatScrollPane, insertMessagePane);
    }
}
