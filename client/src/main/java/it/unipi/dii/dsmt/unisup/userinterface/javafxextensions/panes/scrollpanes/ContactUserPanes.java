package it.unipi.dii.dsmt.unisup.userinterface.javafxextensions.panes.scrollpanes;

import it.unipi.dii.dsmt.unisup.beans.Chat;
import it.unipi.dii.dsmt.unisup.userinterface.CurrentUI;
import it.unipi.dii.dsmt.unisup.userinterface.javafxextensions.panes.regularpanes.ContactSingleResultPane;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

public class ContactUserPanes extends ScrollPane {
    private static VBox root;

    private final String STYLE = "-fx-background: #151e24;";

    public ContactUserPanes(int x, int y, int width, int height) {
        relocate(x, y);
        setPrefSize(width, height);
        setStyle(STYLE);

        root = new VBox();
        root.setSpacing(5);
        setContent(root);

        insertContacts();
    }

    private static void clearResults() {
        root.getChildren().clear();
    }

    public static void insertContacts() {
        clearResults();

        for (Chat c: CurrentUI.getUser().getChatList()) {
            ContactSingleResultPane contactSingleResultPane = new ContactSingleResultPane(c);
            root.getChildren().add(contactSingleResultPane);
        }
    }
}
