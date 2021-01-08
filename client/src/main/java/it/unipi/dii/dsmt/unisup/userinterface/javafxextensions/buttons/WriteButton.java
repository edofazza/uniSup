package it.unipi.dii.dsmt.unisup.userinterface.javafxextensions.buttons;

import javafx.scene.control.Button;

public class WriteButton extends Button {
    public WriteButton(int x, int y) {
        super();
        relocate(x, y);
        setPrefSize(50, 50);
        getStyleClass().add("WriteButton");
    }
}
