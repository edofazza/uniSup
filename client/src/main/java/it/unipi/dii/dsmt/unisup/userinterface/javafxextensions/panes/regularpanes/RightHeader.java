package it.unipi.dii.dsmt.unisup.userinterface.javafxextensions.panes.regularpanes;

import it.unipi.dii.dsmt.unisup.userinterface.javafxextensions.labels.ContactNameLabel;
import javafx.scene.layout.Pane;

public class RightHeader extends Pane {
    private static ContactNameLabel contactName;

    public RightHeader(int x, int y, int width, int height) {
        initContactName();

        relocate(x, y);
        setPrefSize(width, height);
        setStyle("-fx-background-color: #2b3033");
    }

    public void initContactName() {
        contactName = new ContactNameLabel("");

        getChildren().add(contactName);
    }

    public static void changeTextContactName(String name) {
        contactName.setText("~ " + name);
    }

}
