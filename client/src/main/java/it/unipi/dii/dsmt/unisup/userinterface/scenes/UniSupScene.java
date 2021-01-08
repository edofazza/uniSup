package it.unipi.dii.dsmt.unisup.userinterface.scenes;

import javafx.scene.Group;

public class UniSupScene {
    protected static Group sceneNodes = new Group();
    private static String currentTitle;

    /**
     * Get the sceneNodes attribute.
     * @return A <em>Group</em> containing all the <em>Node</em> in the scene
     */
    public Group getNodes() {
        return sceneNodes;
    }
}
