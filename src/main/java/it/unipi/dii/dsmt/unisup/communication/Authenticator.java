package it.unipi.dii.dsmt.unisup.communication;

import it.unipi.dii.dsmt.unisup.beans.User;

public interface Authenticator {

    /**
     * Logins or registers a user to the system
     * @param u user to log/register
     * @return true if the user has been logged, false if he/she has been registered
     */
    boolean loginOrRegister(User u);

    boolean logout(User u);

    boolean addContact(String username);
}
