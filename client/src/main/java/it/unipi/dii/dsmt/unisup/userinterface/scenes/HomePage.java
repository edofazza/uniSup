package it.unipi.dii.dsmt.unisup.userinterface.scenes;

import it.unipi.dii.dsmt.unisup.userinterface.javafxextensions.panes.Container;

public class HomePage extends UniSupScene {
    public HomePage() {
        displayContainer();
    }

    private void displayContainer() {
        sceneNodes.getChildren().add(new Container());
    }
}
