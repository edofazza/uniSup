package it.unipi.dii.dsmt.unisup.beans;

import java.util.*;

public class Chat {
    private final String usernameContact;
    private List<Message> history;

    public Chat(String usernameContact, List<Message> history) {
        this.usernameContact = usernameContact;
        this.history=history;
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
        this.history = history;
    }

    public void addMessageToHistory(Message m){
        history.add(m);
    }
}
