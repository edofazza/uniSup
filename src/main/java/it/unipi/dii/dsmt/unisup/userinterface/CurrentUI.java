package it.unipi.dii.dsmt.unisup.userinterface;

import it.unipi.dii.dsmt.unisup.userinterface.javafxextensions.panes.Container;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;

public class CurrentUI {
    private static CurrentUI currentUI;
    private Group root;

    //****************************
    // SINGLETON
    //****************************
    public CurrentUI() {
        root = new Group();
    }

    public static CurrentUI getInstance() {
        if (currentUI == null)
            currentUI = new CurrentUI();

        return currentUI;
    }

    //****************************
    // SCENE IMPLEMENTATION
    //****************************
    public Scene initScene() {
        Container container = new Container();
        addToGroup(container);

        Scene scene = new Scene(root, 1040, 700);
        scene.getStylesheets().add("file:css/UniSup.css");
        return scene;
    }

    //****************************
    // GROUP OPERATIONS
    //****************************
    public void addToGroup(Node node) {
        root.getChildren().add(node);
    }

    public void removeFromGroup(Node node) {
        root.getChildren().remove(node);
    }
}
