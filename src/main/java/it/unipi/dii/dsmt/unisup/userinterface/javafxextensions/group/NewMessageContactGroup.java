package it.unipi.dii.dsmt.unisup.userinterface.javafxextensions.group;

import it.unipi.dii.dsmt.unisup.userinterface.javafxextensions.buttons.RegularButton;
import it.unipi.dii.dsmt.unisup.userinterface.javafxextensions.labels.FieldRelatedLabel;
import it.unipi.dii.dsmt.unisup.userinterface.javafxextensions.labels.InvalidFormEntryLabel;
import javafx.scene.Group;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class NewMessageContactGroup extends Group {
    private TextArea textArea;
    private InvalidFormEntryLabel invalidFormEntryLabel;

    public NewMessageContactGroup() {

        FieldRelatedLabel contact = new FieldRelatedLabel("Receiver username:", 10, 30);

        TextField receiverTextField = new TextField();
        receiverTextField.relocate(80, 55);

        FieldRelatedLabel message = new FieldRelatedLabel("Message:", 10, 120);

        textArea = new TextArea();
        textArea.relocate(80, 150);
        textArea.setPrefSize(200, 70);

        RegularButton send = new RegularButton("SEND", 280, 225);
        send.setOnAction(e -> sendButtonAction());

        getChildren().addAll(contact, receiverTextField, message, textArea, send);

        addResultLabel();
    }

    private void addResultLabel() {
        invalidFormEntryLabel = new InvalidFormEntryLabel("Receiver not existing", 10, 225, false);

        getChildren().add(invalidFormEntryLabel);
    }

    private void sendButtonAction() {
        // check if the user in present in one of the chat
        if (true) {
            invalidFormEntryLabel.setText("Receiver already in contacts");
        }

        // create new message

        // send to the server

        // create a chat and add it to the contact list
    }
}
