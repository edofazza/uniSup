package it.unipi.dii.dsmt.unisup.userinterface.javafxextensions.panes.regularpanes;

import it.unipi.dii.dsmt.unisup.beans.Chat;
import it.unipi.dii.dsmt.unisup.userinterface.javafxextensions.labels.ContactNameForContactList;
import it.unipi.dii.dsmt.unisup.userinterface.javafxextensions.labels.LastMessageLabel;
import it.unipi.dii.dsmt.unisup.userinterface.javafxextensions.panes.scrollpanes.ChatScrollPane;
import javafx.scene.layout.Pane;

public class ContactSingleResultPane extends Pane {
    private Chat chat;

    private final double WIDTH = 330;
    private final double HEIGHT = 70;
    private final String STYLE = "-fx-border-color: transparent transparent grey transparent;";

    public ContactSingleResultPane(Chat chat) {
        this.chat = chat;

        ContactNameForContactList contactNameForContactList = new ContactNameForContactList("~ " + chat.getUsernameContact());
        setPrefSize(WIDTH, HEIGHT);
        setStyle(STYLE);

        LastMessageLabel lastMessageLabel = new LastMessageLabel(chat.getHistory().get(chat.getHistory().size() - 1).getText());

        getChildren().addAll(contactNameForContactList, lastMessageLabel);

        setOnMouseClicked(e -> seeMessages());
    }

    private void seeMessages() {
        RightHeader.changeTextContactName(chat.getUsernameContact());

        ChatScrollPane.addChat(chat);
    }

}
