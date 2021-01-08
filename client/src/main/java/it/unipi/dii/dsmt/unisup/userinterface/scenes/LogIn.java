package it.unipi.dii.dsmt.unisup.userinterface.scenes;

import it.unipi.dii.dsmt.unisup.beans.User;
import it.unipi.dii.dsmt.unisup.communication.AuthGateway;
import it.unipi.dii.dsmt.unisup.communication.Authenticator;
import it.unipi.dii.dsmt.unisup.userinterface.CurrentUI;
import it.unipi.dii.dsmt.unisup.userinterface.enumui.SceneNames;
import it.unipi.dii.dsmt.unisup.userinterface.javafxextensions.buttons.RegularButton;
import it.unipi.dii.dsmt.unisup.userinterface.javafxextensions.labels.FieldRelatedLabel;
import it.unipi.dii.dsmt.unisup.userinterface.javafxextensions.labels.InvalidFormEntryLabel;
import it.unipi.dii.dsmt.unisup.userinterface.xml.XMLParser;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * Class scene related to the Log In page
 */
public class LogIn extends UniSupScene {
    private TextField usernameTF;
    private PasswordField passwordTF;
    private InvalidFormEntryLabel invalidFormEntryLabel;

    /**
     * Constructor. Called a series of functions to add all the <code>Node</code> needed.
     * It also sets the music.
     */
    public LogIn() {
        displayEmailFields();
        displayPasswordFields();

        displayResultLabel();

        displayRegisterButton();
        displayLogInButton();
    }

    private void displayResultLabel() {
        invalidFormEntryLabel = new InvalidFormEntryLabel("", 470, 400, false);

        sceneNodes.getChildren().add(invalidFormEntryLabel);
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

        logInButton.setOnAction( e -> logInButtonAction());

        sceneNodes.getChildren().add(logInButton);
    }

    /**
     * Checks if the fields filled by the user are correct and if so, it goes to his homepage.
     */
    private void logInButtonAction() {

        User user = new User(usernameTF.getText(), passwordTF.getText());

        Authenticator auth = AuthGateway.getInstance();
        boolean result = auth.login(user);
        System.out.println("Result of login is" + result);

        if(result) {
            CurrentUI.setUser(user);
            CurrentUI.changeScene(SceneNames.HOMEPAGE);
        } else {
            invalidFormEntryLabel.setText("Username/Password not correct");
            invalidFormEntryLabel.setStyle("-fx-background-color: #FF211A;");
            invalidFormEntryLabel.setVisible(true);
        }
    }

    private void displayRegisterButton() {
        RegularButton registerButton = new RegularButton("REGISTER", 350, 370);
        registerButton.setOnAction(e -> registerButtonAction());

        sceneNodes.getChildren().add(registerButton);
    }

    private void registerButtonAction() {
        Authenticator auth = AuthGateway.getInstance();
        User user = new User(usernameTF.getText(), passwordTF.getText());
        boolean result = auth.register(user);

        if (result) {
            invalidFormEntryLabel.setText("User added");
            invalidFormEntryLabel.setStyle("-fx-background-color: green;");
        } else {
            invalidFormEntryLabel.setText("Username already taken");
            invalidFormEntryLabel.setStyle("-fx-background-color: #FF211A;");
        }
        invalidFormEntryLabel.setVisible(true);
    }
}
