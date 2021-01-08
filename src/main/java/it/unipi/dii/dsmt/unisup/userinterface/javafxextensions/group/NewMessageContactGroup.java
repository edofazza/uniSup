package it.unipi.dii.dsmt.unisup.userinterface.javafxextensions.group;

import it.unipi.dii.dsmt.unisup.beans.Chat;
import it.unipi.dii.dsmt.unisup.beans.Message;
import it.unipi.dii.dsmt.unisup.communication.MessageGateway;
import it.unipi.dii.dsmt.unisup.userinterface.CurrentUI;
import it.unipi.dii.dsmt.unisup.userinterface.javafxextensions.buttons.RegularButton;
import it.unipi.dii.dsmt.unisup.userinterface.javafxextensions.labels.FieldRelatedLabel;
import it.unipi.dii.dsmt.unisup.userinterface.javafxextensions.labels.InvalidFormEntryLabel;
import it.unipi.dii.dsmt.unisup.userinterface.javafxextensions.panes.scrollpanes.ContactUserPanes;
import javafx.scene.Group;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.List;

public class NewMessageContactGroup extends Group {
    private FieldRelatedLabel contact;
    private TextArea textArea;
    private InvalidFormEntryLabel invalidFormEntryLabel;

    public NewMessageContactGroup() {
        contact = new FieldRelatedLabel("Receiver username:", 10, 30);

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
        if (CurrentUI.getUser().userAlreadyPresent(CurrentUI.getUser().getUsername())) {
            invalidFormEntryLabel.setText("Receiver already in contacts");
        }

        // create new message
        Message message = new Message(CurrentUI.getUser().getUsername(), contact.getText(), textArea.getText());

        // send to the server
        MessageGateway messageGateway = MessageGateway.getInstance();
        messageGateway.sendMessage(message, 5000);

        List<Message> messages = new ArrayList<>();
        messages.add(message);
        // create a chat and add it to the contact list
        Chat chat = new Chat(CurrentUI.getUser().getUsername(), messages);

        CurrentUI.getUser().addChat(chat);

        ContactUserPanes.insertContacts();
    }
}
