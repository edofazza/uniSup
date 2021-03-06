package it.unipi.dii.dsmt.unisup.beans;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class User {
    private String username;
    private String password;

    private List<Chat> chatList;

    public boolean needToUpdate = false;

    public User(String username, String password){
        this.username=username;
        this.password=password;

        this.chatList = Collections.synchronizedList(new ArrayList<>());
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
        this.chatList = Collections.synchronizedList(chatList);
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Chat insertMessage(Message m) {
        boolean chatAlreadyPresent = false;

        String contact = m.getReceiver().equals(username) ? m.getSender() : m.getReceiver();
        Chat chat = null;
        //loop need explicit synchronization
        synchronized (chatList) {
            for (Chat c : chatList) {
                if (c.getUsernameContact().equals(contact)) {
                    chatAlreadyPresent = true;
                    chat = c;
                    break;
                }
            }
        }

        if (!chatAlreadyPresent) {
            List<Message> list = new ArrayList<>();
            list.add(m);
            chat = new Chat(contact, list);
            chatList.add(chat);
        } else
            chat.addMessageToHistory(m);

        // RELOAD EVERYTHING IN THE SCENE, EVEN THE CHAT IS CURRENTLY DISPLAYING
        //ContactUserPanes.insertContacts();
        //ChatScrollPane.addChat(ChatScrollPane.getChat());
        needToUpdate = true;
        return chat;
    }

    public boolean userAlreadyPresent(String contact) {
        synchronized (chatList) {
            for (Chat c : chatList) {
                if (c.getUsernameContact().equals(contact))
                    return true;
            }
            return false;
        }
    }

    public void addChat(Chat chat) {
        chatList.add(chat);
    }
}
