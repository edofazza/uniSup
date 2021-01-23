package it.unipi.dii.dsmt.unisup.beans;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Chat {
    private final String usernameContact;
    private List<Message> history;
    private int unreadMessages = 0;

    public Chat(String usernameContact, List<Message> history) {
        this.usernameContact = usernameContact;
        this.history= Collections.synchronizedList(history);
    }

    public Chat(String usernameContact, List<Message> history, int unreadMessages){
        this.usernameContact=usernameContact;
        this.history= Collections.synchronizedList(history);
        this.unreadMessages=unreadMessages;
    }

    public Chat(String usernameContact){
        this(usernameContact, new ArrayList<>());
    }

    public String getUsernameContact() {
        return usernameContact;
    }

    public List<Message> getHistory() {
        return history;
    }

    public void setHistory(List<Message> history) {
        this.history = Collections.synchronizedList(history);
    }

    public void addMessageToHistory(Message m){
        history.add(m);
    }

    public int getUnreadMessages() {
        return unreadMessages;
    }

    public void increaseUnreadMessages(int unreadMessages) {
        this.unreadMessages += unreadMessages;
    }
    public void readAllMessages() {
        this.unreadMessages = 0;
    }

    public boolean hasUnreadMessages(){
        return this.unreadMessages > 0;
    }
    @Override
    public String toString() {
        return  usernameContact;
    }
}
