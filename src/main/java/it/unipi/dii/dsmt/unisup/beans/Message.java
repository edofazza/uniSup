package it.unipi.dii.dsmt.unisup.beans;

import java.time.Instant;

public class Message {
    private String sender;
    private String receiver;
    private Instant timestamp;
    private String text;

    public Message(String sender, String receiver, String text){
        this.receiver=receiver;
        this.sender=sender;
        this.text=text;
        this.timestamp= Instant.now();
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getSender() {
        return sender;
    }

    public String getText() {
        return text;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }
}
