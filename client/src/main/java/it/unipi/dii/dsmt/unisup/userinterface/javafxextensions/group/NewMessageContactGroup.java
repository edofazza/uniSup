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
    private TextField receiverTextField;
    private TextArea textArea;
    private InvalidFormEntryLabel invalidFormEntryLabel;

    public NewMessageContactGroup() {
        FieldRelatedLabel contact = new FieldRelatedLabel("Receiver username:", 10, 30);

        receiverTextField = new TextField();
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
        if (CurrentUI.getUser().userAlreadyPresent(receiverTextField.getText())) {
            invalidFormEntryLabel.setText("Receiver already in contacts");
            invalidFormEntryLabel.setVisible(true);
            return;
        }

        // create new message
        Message message = new Message(CurrentUI.getUser().getUsername(), receiverTextField.getText(), textArea.getText());
        System.out.println(receiverTextField.getText());

        // send to the server
        MessageGateway messageGateway = MessageGateway.getInstance();
        messageGateway.sendMessage(message, 5000);

        List<Message> messages = new ArrayList<>();
        messages.add(message);
        // create a chat and add it to the contact list
        Chat chat = new Chat(receiverTextField.getText(), messages);

        CurrentUI.getUser().addChat(chat);

        ContactUserPanes.insertContacts();
    }
}
