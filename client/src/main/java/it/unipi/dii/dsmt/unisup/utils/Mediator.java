package it.unipi.dii.dsmt.unisup.utils;

import it.unipi.dii.dsmt.unisup.beans.Chat;
import it.unipi.dii.dsmt.unisup.beans.Message;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;

public class Mediator {

    private static ListView<Chat>  contactList;
    private static ObservableList<Chat> contactObsList;
    private static ListView<Message> historyList;
    private static ObservableList<Message> histObsList;

    public static ListView<Chat> getContactList() {
        return contactList;
    }

    public static void setContactList(ListView<Chat> contactList) {
        Mediator.contactList = contactList;
    }

    public static ObservableList<Chat> getContactObsList() {
        return contactObsList;
    }

    public static void setContactObsList(ObservableList<Chat> contactObsList) {
        Mediator.contactObsList = contactObsList;
    }

    public static ListView<Message> getHistoryList() {
        return historyList;
    }

    public static void setHistoryList(ListView<Message> historyList) {
        Mediator.historyList = historyList;
    }

    public static ObservableList<Message> getHistObsList() {
        return histObsList;
    }

    public static void setHistObsList(ObservableList<Message> histObsList) {
        Mediator.histObsList = histObsList;
    }

}
