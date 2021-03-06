package it.unipi.dii.dsmt.unisup.beans;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

public class Message {
    private static AtomicInteger identifier = new AtomicInteger(0);
    private final int messageId;
    private String sender;
    private String receiver;
    private Instant timestamp;
    private String text;

    public Message(String sender, String receiver, String text){
        this(sender, receiver, text, Instant.now());
    }

    public Message(String sender, String receiver, String text, Instant timestamp){
        this.messageId = identifier.getAndIncrement();
        this.receiver = receiver;
        this.sender = sender;
        this.text = text;
        this.timestamp = timestamp;
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


    public int getMessageId() {
        return messageId;
    }

    @Override
    public String toString() {
        return  sender+"\n" + text + "\t" + timestamp;
    }

    public String getFormattedTimestamp(){
        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm a")
                .withZone(ZoneId.systemDefault());
        return df.format(timestamp);
    }
}
