package it.unipi.dii.dsmt.unisup.utils;

import it.unipi.dii.dsmt.unisup.beans.Chat;
import javafx.scene.control.ListView;

public class Mediator {

    private static ListView<Chat>  messagesList;

    public static void setMessagesList(ListView<Chat> messagesList) {
        Mediator.messagesList = messagesList;
    }

    public static ListView<Chat> getMessagesList() {
        return messagesList;
    }
}
