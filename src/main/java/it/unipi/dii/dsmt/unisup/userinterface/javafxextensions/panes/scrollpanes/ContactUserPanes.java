package it.unipi.dii.dsmt.unisup.userinterface.javafxextensions.panes.scrollpanes;

import it.unipi.dii.dsmt.unisup.userinterface.javafxextensions.panes.regularpanes.ContactSingleResultPane;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

public class ContactUserPanes extends ScrollPane {
    private VBox root;

    public ContactUserPanes(int x, int y, int width, int height) {
        relocate(x, y);
        setPrefSize(width, height);
        setStyle("-fx-background: #151e24;");

        root = new VBox();
        root.setSpacing(5);
        setContent(root);

        insertContacts();
    }

    private void clearResults() {
        root.getChildren().clear();
    }

    private void insertContacts() {
        clearResults();

        for (int i = 0; i < 4; i++) {
            ContactSingleResultPane contactSingleResultPane = new ContactSingleResultPane("Scrooge McDuck " + i, "Last message test super long goofy" + i);
            root.getChildren().add(contactSingleResultPane);
        }
    }
}
