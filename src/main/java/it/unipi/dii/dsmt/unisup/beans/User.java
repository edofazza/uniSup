package it.unipi.dii.dsmt.unisup.beans;

import java.util.List;

public class User {
    private String username;
    private String password;

    private List<Chat> chatList;

    public User(String username, String password){
        this.username=username;
        this.password=password;
    }

    public String getUsername() {
        return  username;
    }

    public String getPassword() {
        return password;
    }

    public List<Chat> getChatList() {
        return chatList;
    }

    public void setChatList(List<Chat> chatList) {
        this.chatList = chatList;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void insertMessage(Message m) {
        System.out.println("okay");
    }

    public boolean userAlreadyPresent(String contact) {
        for (Chat c: chatList) {
            if (c.getUsernameContact().equals(contact))
                return true;
        }
        return false;
    }

}
