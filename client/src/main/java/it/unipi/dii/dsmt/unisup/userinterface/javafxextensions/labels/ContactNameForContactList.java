package it.unipi.dii.dsmt.unisup.userinterface.javafxextensions.labels;

import javafx.scene.control.Label;

public class ContactNameForContactList extends ContactNameLabel {
    public ContactNameForContactList(String text) {
        super(text);
        setStyle("-fx-font-size: 15px;");
        relocate(5, 5);
    }
}
