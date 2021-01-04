package it.unipi.dii.dsmt.unisup.userinterface.javafxextensions.buttons;

import javafx.scene.control.Button;

public class LogOutButton extends Button {
    public LogOutButton(String text, int x, int y) {
        super(text);
        relocate(x, y);

        getStyleClass().add("LogOutButton");
    }
}
