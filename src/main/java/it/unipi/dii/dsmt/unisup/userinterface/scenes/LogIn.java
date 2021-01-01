package it.unipi.dii.dsmt.unisup.userinterface.scenes;

import it.unipi.dii.dsmt.unisup.beans.Message;
import it.unipi.dii.dsmt.unisup.communication.MessageGateway;
import it.unipi.dii.dsmt.unisup.userinterface.CurrentUI;
import it.unipi.dii.dsmt.unisup.userinterface.enumui.SceneNames;
import it.unipi.dii.dsmt.unisup.userinterface.javafxextensions.buttons.RegularButton;
import it.unipi.dii.dsmt.unisup.userinterface.javafxextensions.labels.FieldRelatedLabel;
import it.unipi.dii.dsmt.unisup.userinterface.javafxextensions.labels.InvalidFormEntryLabel;
import javafx.event.ActionEvent;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * Class scene related to the Log In page
 */
public class LogIn extends UniSupScene {
    private TextField usernameTF;
    private PasswordField passwordTF;

    /**
     * Constructor. Called a series of functions to add all the <code>Node</code> needed.
     * It also sets the music.
     */
    public LogIn() {
        displayEmailFields();
        displayPasswordFields();

        displayLogInButton();
    }

    ////////////////////////  FIELDS  ////////////////////////

    /**
     * Display the Node related to the Email: the <code>FieldRelatedLabel</code> and the <code>FieldRelatedLabel</code>
     */
    private void displayEmailFields() {
        FieldRelatedLabel usernameLabel = new FieldRelatedLabel("Username", 420, 170);

        usernameTF = new TextField();
        usernameTF.relocate(420, 200);

        sceneNodes.getChildren().addAll(usernameLabel, usernameTF);
    }

    /**
     * Display the Node related to the Password: the <code>FieldRelatedLabel</code> and the <code>FieldRelatedLabel</code>
     */
    private void displayPasswordFields() {
        FieldRelatedLabel passwordLabel = new FieldRelatedLabel("Password", 420, 270);

        passwordTF = new PasswordField();
        passwordTF.relocate(420, 300);

        sceneNodes.getChildren().addAll(passwordLabel, passwordTF);
    }

    ////////////////////////  BUTTONS  ////////////////////////

    /**
     * Add to the scene the <code>RegularButton</code> for the log in.
     */
    private void displayLogInButton() {
        RegularButton logInButton = new RegularButton("LOG IN", 570, 370);

        logInButton.setOnAction((ActionEvent ev)-> logInButtonAction());

        sceneNodes.getChildren().add(logInButton);
    }

    /**
     * Checks if the fields filled by the user are correct and if so, it goes to his homepage.
     */
    private void logInButtonAction() {
        // set the user
        if(true) { //TODO
            //MessageGateway messageGateway = MessageGateway.getInstance();
            //messageGateway.sendMessage(new Message("unisup_client_node@localhost", "donald@localhost", "Test"), 5);
            CurrentUI.changeScene(SceneNames.HOMEPAGE);
        } else {
            InvalidFormEntryLabel loginError = new InvalidFormEntryLabel("Username/password incorrect", 470, 400, true);
            sceneNodes.getChildren().add(loginError);
        }
    }
}
