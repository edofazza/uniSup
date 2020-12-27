package it.unipi.dii.dsmt.unisup.userinterface.enumui;

import it.unipi.dii.dsmt.unisup.userinterface.scenes.LogIn;
import it.unipi.dii.dsmt.unisup.userinterface.scenes.UniSupScene;

/**
 * Contains all the names of the scenes.
 */
public enum SceneNames {
    LOGIN, HOMEPAGE;

    /**
     * This function handles the creation of the class scene asked.
     * @param sn is the SceneName we want to create.
     * @return The class scene asked.
     */
    public UniSupScene createNewScene(SceneNames sn) {
        switch (sn) {
            case LOGIN:
                return new LogIn();
            //case HOMEPAGE:
               // return new HomePage();
            default:
                return null;
        }
    }
}
