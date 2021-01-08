package it.unipi.dii.dsmt.unisup.userinterface;

import it.unipi.dii.dsmt.unisup.beans.Message;
import it.unipi.dii.dsmt.unisup.beans.User;
import it.unipi.dii.dsmt.unisup.communication.MessageGateway;
import it.unipi.dii.dsmt.unisup.userinterface.enumui.SceneNames;
import it.unipi.dii.dsmt.unisup.userinterface.scenes.LogIn;
import it.unipi.dii.dsmt.unisup.userinterface.scenes.UniSupScene;
import javafx.scene.Group;
import javafx.scene.Scene;


public class CurrentUI {
    private static UniSupScene nodeWindow;
    private static Group root;
    private static User userLogged;

    /**
     * Create the default scene (<em>LogIn</em>) and set the dimension of it
     * @return the scene created
     */
    public Scene initScene() {
        /*   LogIn   */

        nodeWindow = new LogIn();
        root = nodeWindow.getNodes();

        Scene scene = new Scene(root, 1040, 700);
        scene.getStylesheets().add("file:css/UniSup.css");
        return scene;
    }

    /**
     * Clears all the nodes from the scene, and then adds the nodes regarding the new one
     * @param sn is a <em>SceneNames</em> which indicates the new scene
     */

    public static void changeScene(SceneNames sn) {

        root.getChildren().clear();
        nodeWindow = sn.createNewScene(sn);
        root = nodeWindow.getNodes();
    }

    /**
     * Get the user logged
     * @return the user logged
     */
    public static User getUser() {
        return userLogged;
    }

    public static void setUser(User user) {
        userLogged = user;
    }

    /**
     * Removes the user stored in memory (<code>userLogged</code>) when the user logged out (in order to avoid error and to be coherent).
     */
    protected static void userExit() {
        userLogged = null;
    }

    private static void startListener() {
       while (true) {
           MessageGateway messageGateway = MessageGateway.getInstance();
           Message m = messageGateway.receiveMessage();

           // INSERT IT INTO USER
           if (userLogged == null)
               return;

           userLogged.insertMessage(m);
       }

    }
}
