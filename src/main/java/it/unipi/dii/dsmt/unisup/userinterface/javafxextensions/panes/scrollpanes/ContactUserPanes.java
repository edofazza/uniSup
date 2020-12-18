package it.unipi.dii.dsmt.unisup.userinterface.javafxextensions.panes.scrollpanes;

import javafx.scene.control.ScrollPane;

public class ContactUserPanes extends ScrollPane {

    public ContactUserPanes(int x, int y, int width, int height) {
        relocate(x, y);
        setPrefSize(width, height);
        setStyle("-fx-background: #151e24;");
    }
}
