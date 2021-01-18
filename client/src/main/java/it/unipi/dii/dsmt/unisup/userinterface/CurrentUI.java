package it.unipi.dii.dsmt.unisup.userinterface;

import it.unipi.dii.dsmt.unisup.beans.Message;
import it.unipi.dii.dsmt.unisup.beans.User;
import it.unipi.dii.dsmt.unisup.communication.MessageGateway;
import it.unipi.dii.dsmt.unisup.userinterface.enumui.SceneNames;
import it.unipi.dii.dsmt.unisup.userinterface.javafxextensions.panes.scrollpanes.ChatScrollPane;
import it.unipi.dii.dsmt.unisup.userinterface.scenes.LogIn;
import it.unipi.dii.dsmt.unisup.userinterface.scenes.UniSupScene;
import javafx.concurrent.Task;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.TextField;

import java.util.List;


public class CurrentUI {
    private static UniSupScene nodeWindow;
    private static Group root;
    private static User userLogged;
    private static boolean listenerStarted = false;
    private static Thread thread;

    private final double SCENE_WIDTH = 1040;
    private final double SCENE_HEIGHT = 700;

    /**
     * Create the default scene (<em>LogIn</em>) and set the dimension of it
     * @return the scene created
     */
    public Scene initScene() {
        /*   LogIn   */
        nodeWindow = new LogIn();
        root = nodeWindow.getNodes();

        Scene scene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);
        scene.getStylesheets().add("file:client/css/UniSup.css");
        return scene;
    }

    /**
     * Clears all the nodes from the scene, and then adds the nodes regarding the new one
     * @param sn is a <em>SceneNames</em> which indicates the new scene
     */

    public static void changeScene(SceneNames sn) {
        listenerStarted = !listenerStarted;

        if (listenerStarted)
            startListener();

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
    public static void userExit() {
        userLogged = null;
        thread.interrupt();
        thread = null;
    }

    private static void startListener() {
        thread = new Thread(new ListenerTask());
        thread.start();
    }


    static class ListenerTask implements Runnable {

        @Override
        public void run() {
            while (true) {
                MessageGateway messageGateway = MessageGateway.getInstance();
                Message m = messageGateway.receiveMessage();

                // INSERT IT INTO USER
                if (userLogged == null)
                    return;

                userLogged.insertMessage(m);
                new Thread(new ListenerTaskJavaFx()).start();
            }
        }
    }


    static class ListenerTaskJavaFx extends Task<Boolean> {

        @Override
        protected Boolean call() throws Exception {

            //ContactUserPanes.insertContacts();


            List<Message> c = userLogged.getChatList().get(0).getHistory();
            System.out.println("Il messaggio ricevuto è " + c.get(c.size()-1).getText());
            ChatScrollPane.ghost.setText("A");

            return true;
        }
    }
}
