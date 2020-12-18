package it.unipi.dii.dsmt.unisup.userinterface.javafxextensions.panes;

import it.unipi.dii.dsmt.unisup.userinterface.javafxextensions.panes.regularpanes.LeftHeaderWithCreateNewChat;
import it.unipi.dii.dsmt.unisup.userinterface.javafxextensions.panes.regularpanes.RightHeader;

import it.unipi.dii.dsmt.unisup.userinterface.javafxextensions.panes.scrollpanes.ContactUserPanes;
import javafx.scene.layout.Pane;

public class Container extends Pane {

    public Container() {
        setStyle("-fx-background-color: #151e24");
        displayLeftHeader();
        displayContactUserPane();
        displayRightHeader();
    }

    private void displayLeftHeader() {
        LeftHeaderWithCreateNewChat leftHeaderWithCreateNewChat = new LeftHeaderWithCreateNewChat(-1, -1, 350, 51);

        getChildren().add(leftHeaderWithCreateNewChat);
    }

    private void displayContactUserPane() {
        ContactUserPanes contactUserPanes = new ContactUserPanes(-1, 50, 350, 651);

        getChildren().add(contactUserPanes);
    }

    private void displayRightHeader() {
        RightHeader rightHeader = new RightHeader(350, -1, 720, 51);

        getChildren().add(rightHeader);
    }


}
