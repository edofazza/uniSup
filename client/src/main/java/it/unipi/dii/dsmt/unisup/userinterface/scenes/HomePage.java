package it.unipi.dii.dsmt.unisup.userinterface.scenes;

import it.unipi.dii.dsmt.unisup.userinterface.CurrentUI;
import it.unipi.dii.dsmt.unisup.userinterface.javafxextensions.panes.Container;
import it.unipi.dii.dsmt.unisup.userinterface.javafxextensions.panes.scrollpanes.ChatScrollPane;
import it.unipi.dii.dsmt.unisup.userinterface.javafxextensions.panes.scrollpanes.ContactUserPanes;

public class HomePage extends UniSupScene {
    public HomePage() {
        displayContainer();
        
        while (CurrentUI.getUser().needToUpdate) {
            ContactUserPanes.insertContacts();
            ChatScrollPane.addChat(ChatScrollPane.getChat());
            CurrentUI.getUser().needToUpdate = false;
        }
    }

    private void displayContainer() {
        sceneNodes.getChildren().add(new Container());
    }
}
