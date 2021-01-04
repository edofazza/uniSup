package it.unipi.dii.dsmt.unisup.communication;

import it.unipi.dii.dsmt.unisup.beans.Chat;
import it.unipi.dii.dsmt.unisup.beans.User;

import java.util.List;

public interface Authenticator {

    /**
     * Logins or registers a user to the system
     * @param u user to log/register
     * @return true if the user has been logged, false if he/she has been registered
     */
    boolean login(User u);

    List<Chat> getChatHistory(User u);

    boolean register(User u);

    boolean logout(User u);

    //boolean addContact(String username);
}
