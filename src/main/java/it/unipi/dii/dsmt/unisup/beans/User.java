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


    public void setPassword(String password) {
        this.password = password;
    }

}
