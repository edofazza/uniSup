package it.unipi.dii.dsmt.unisup.userinterface.javafxextensions.panes;

import it.unipi.dii.dsmt.unisup.userinterface.javafxextensions.panes.regularpanes.ChatPane;
import it.unipi.dii.dsmt.unisup.userinterface.javafxextensions.panes.regularpanes.LeftHeaderWithCreateNewChat;
import it.unipi.dii.dsmt.unisup.userinterface.javafxextensions.panes.regularpanes.RightHeader;
import it.unipi.dii.dsmt.unisup.userinterface.javafxextensions.panes.scrollpanes.ContactUserPanes;
import javafx.scene.layout.Pane;

public class Container extends Pane {
    private final String STYLE =  "-fx-background-color: #151e24";

    private final int LEFT_START_POS = -1;
    private final int UPPER_START_POS = -1;
    private final int RIGHT_START_POS = 350;
    private final int LOWER_START_POS = 50;

    private final int LEFT_WIDTH = 350;
    private final int RIGHT_WIDTH = 720;
    private final int UPPER_HEIGHT = 51;
    private final int LOWER_HEIGHT = 651;

    public Container() {
        setStyle(STYLE);
        displayLeftHeader();
        displayContactUserPane();
        displayRightHeader();
        displayChatPane();
    }

    private void displayLeftHeader() {
        LeftHeaderWithCreateNewChat leftHeaderWithCreateNewChat = new LeftHeaderWithCreateNewChat(LEFT_START_POS, UPPER_START_POS, LEFT_WIDTH, UPPER_HEIGHT);

        getChildren().add(leftHeaderWithCreateNewChat);
    }

    private void displayContactUserPane() {
        ContactUserPanes contactUserPanes = new ContactUserPanes(LEFT_START_POS, LOWER_START_POS, LEFT_WIDTH, LOWER_HEIGHT);

        getChildren().add(contactUserPanes);
    }

    private void displayRightHeader() {
        RightHeader rightHeader = new RightHeader(RIGHT_START_POS, UPPER_START_POS, RIGHT_WIDTH, UPPER_HEIGHT);

        getChildren().add(rightHeader);
    }

    private void displayChatPane() {
        ChatPane chatPane = new ChatPane(RIGHT_START_POS, LOWER_START_POS, RIGHT_WIDTH, LOWER_HEIGHT);

        getChildren().add(chatPane);
    }

}
