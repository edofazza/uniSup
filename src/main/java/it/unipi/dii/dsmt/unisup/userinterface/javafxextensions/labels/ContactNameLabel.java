package it.unipi.dii.dsmt.unisup.userinterface.javafxextensions.labels;

import javafx.scene.control.Label;

public class ContactNameLabel extends Label {
    public ContactNameLabel(String text) {
        super(text);
        setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-family: 'Arial Black'; -fx-font-size: 25px;");
        relocate(20, 5);
    }
}
