package it.unipi.dii.dsmt.unisup.userinterface.javafxextensions.panes.regularpanes;

import it.unipi.dii.dsmt.unisup.userinterface.javafxextensions.buttons.WriteButton;
import javafx.scene.layout.Pane;

public class LeftHeaderWithCreateNewChat extends Pane {
    public LeftHeaderWithCreateNewChat(int x, int y, int width, int height) {
        relocate(x, y);
        setPrefSize(width, height);
        setStyle("-fx-background-color: #2b3033;");

        displayWriteButton();
    }

    private void displayWriteButton() {
        WriteButton writeButton = new WriteButton(20, 0);

        getChildren().add(writeButton);
    }
}
