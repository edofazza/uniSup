package it.unipi.dii.dsmt.unisup.utils;

import it.unipi.dii.dsmt.unisup.beans.Chat;
import it.unipi.dii.dsmt.unisup.beans.Message;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

import javax.xml.soap.Text;

public class Mediator {

    private static ListView<Chat>  contactList;
    private static ObservableList<Chat> contactObsList;
    private static ListView<Message> historyList;
    private static ObservableList<Message> histObsList;
    private static Button MainFrameSendButton;
    private static TextArea MainFrameMsgTextArea;



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

    public static Button getMainFrameSendButton() { return MainFrameSendButton; }

    public static TextArea getMainFrameMsgTextArea() { return MainFrameMsgTextArea; }

    public static void setMainFrameSendButton(Button MainFrameSendButton) {
        Mediator.MainFrameSendButton = MainFrameSendButton;
    }

    public static void setMainFrameMsgTextArea(TextArea MainFrameMsgTextArea) {
        Mediator.MainFrameMsgTextArea = MainFrameMsgTextArea;
    }
}
