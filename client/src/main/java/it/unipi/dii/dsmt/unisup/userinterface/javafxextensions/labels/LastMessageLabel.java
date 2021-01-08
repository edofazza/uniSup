package it.unipi.dii.dsmt.unisup.userinterface.javafxextensions.labels;

import javafx.scene.control.Label;

public class LastMessageLabel extends Label {
    public LastMessageLabel(String text) {
        super();
        setStyle("-fx-text-fill: grey; -fx-font-family: 'Arial'; -fx-font-size: 12px;");
        relocate(30, 30);

        if (text.length() > 30)
            text = text.substring(0, 30) + "...";

        setText(text);
    }
}
