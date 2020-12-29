package it.unipi.dii.dsmt.unisup.auth;

import it.unipi.dii.dsmt.unisup.beans.User;

public abstract class UnisupAuthenticator implements Authenticator{
    @Override
    public boolean loginOrRegister(User u){
        boolean result = authenticate(u);
        loadBackups(u);
        return result;
    }

    @Override
    public boolean logout(User u) {
        return saveBackups(u);
    }

    @Override
    public abstract boolean addContact(String username);

    public abstract boolean authenticate(User u);
    public abstract void loadBackups(User u);
    public abstract boolean saveBackups(User u);
}
