package it.unipi.dii.dsmt.unisup.userinterface.javafxextensions.panes.regularpanes;

import it.unipi.dii.dsmt.unisup.beans.Chat;
import it.unipi.dii.dsmt.unisup.beans.Message;
import it.unipi.dii.dsmt.unisup.userinterface.javafxextensions.labels.ContactNameForContactList;
import it.unipi.dii.dsmt.unisup.userinterface.javafxextensions.labels.LastMessageLabel;
import it.unipi.dii.dsmt.unisup.userinterface.javafxextensions.panes.scrollpanes.ChatScrollPane;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;

public class ContactSingleResultPane extends Pane {
    private String contactName;
    private String lastMessage;

    public ContactSingleResultPane(String contactName, String lastMessage) {
        this.contactName = contactName;
        this.lastMessage = lastMessage;

        ContactNameForContactList contactNameForContactList = new ContactNameForContactList("~ " + contactName);
        setPrefSize(330, 70);
        setStyle("-fx-border-color: transparent transparent grey transparent;");

        LastMessageLabel lastMessageLabel = new LastMessageLabel(lastMessage);

        getChildren().addAll(contactNameForContactList, lastMessageLabel);

        setOnMouseClicked(e -> seeMessages());
    }

    private void seeMessages() {
        RightHeader.changeTextContactName(contactName);

        List<Message> messages = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            Message message;
            if ( i % 2 == 0)
                message = new Message("me", contactName, "qwertyuiopoiuytfdsfdghdgmgffsgsjgididifjdifjdijfidjffjdifjidjfidjdijididifidjfidjfidjfjdijfdijfidfjdi");
            else
                message = new Message(contactName, "me", "PIPPO PIPPO PIPPO PIPPO PIPPO PIPPO PIPPO PIPPO PIPPO PIPPO PIPPO PIPPO PIPPO PIPPO PIPPO PIPPO PIPPO PIPPO");
            messages.add(message);
        }

        Chat chat = new Chat(contactName, messages);

        ChatScrollPane.addChat(chat);
    }

}
