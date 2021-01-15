package it.unipi.dii.dsmt.unisup.userinterface.scenes;

import it.unipi.dii.dsmt.unisup.beans.User;
import it.unipi.dii.dsmt.unisup.communication.AuthGateway;
import it.unipi.dii.dsmt.unisup.communication.Authenticator;
import it.unipi.dii.dsmt.unisup.userinterface.CurrentUI;
import it.unipi.dii.dsmt.unisup.userinterface.enumui.SceneNames;
import it.unipi.dii.dsmt.unisup.userinterface.javafxextensions.buttons.RegularButton;
import it.unipi.dii.dsmt.unisup.userinterface.javafxextensions.labels.FieldRelatedLabel;
import it.unipi.dii.dsmt.unisup.userinterface.javafxextensions.labels.InvalidFormEntryLabel;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * Class scene related to the Log In page
 */
public class LogIn extends UniSupScene {
    private TextField usernameTF;
    private PasswordField passwordTF;
    private InvalidFormEntryLabel invalidFormEntryLabel;

    private final int RESULT_LABEL_X_POS = 470;
    private final int RESULT_LABEL_Y_POS = 400;
    private final String RESULT_LABEL_STYLE_GREEN = "-fx-background-color: green;";
    private final String RESULT_LABEL_STYLE_RED = "-fx-background-color: #FF211A;";
    private final String RESULT_LABEL_NOTCOR = "Username/Password not correct";
    private final String RESULT_LABEL_TAKEN = "Username already taken";
    private final String RESULT_LABEL_SUCCESS = "User added";

    private final String USERNAME_LABEL_TEXT = "Username";
    private final int USERNAME_LABEL_X_POS = 420;
    private final int USERNAME_LABEL_Y_POS = 170;

    private final int USERNAME_TF_X_POS = 420;
    private final int USERNAME_TF_Y_POS = 200;

    private final String PASSWORD_LABEL_TEXT = "Password";
    private final int PASSWORD_LABEL_X_POS = 420;
    private final int PASSWORD_LABEL_Y_POS = 270;

    private final int PASSWORD_TF_X_POS = 420;
    private final int PASSWORD_TF_Y_POS = 300;

    private final String LOGIN_BUTTON_TEXT = "LOG IN";
    private final int LOGIN_BUTTON_X_POS = 570;
    private final int LOGIN_BUTTON_Y_POS = 370;

    private final String REGISTER_BUTTON_TEXT = "REGISTER";
    private final int REGISTER_BUTTON_X_POS = 350;
    private final int REGISTER_BUTTON_Y_POS = 370;

    /**
     * Constructor. Called a series of functions to add all the <code>Node</code> needed.
     * It also sets the music.
     */
    public LogIn() {
        displayUsernameFields();
        displayPasswordFields();

        displayResultLabel();

        displayRegisterButton();
        displayLogInButton();
    }

    private void displayResultLabel() {
        invalidFormEntryLabel = new InvalidFormEntryLabel("", RESULT_LABEL_X_POS, RESULT_LABEL_Y_POS, false);

        sceneNodes.getChildren().add(invalidFormEntryLabel);
    }

    ////////////////////////  FIELDS  ////////////////////////

    /**
     * Display the Node related to the Email: the <code>FieldRelatedLabel</code> and the <code>FieldRelatedLabel</code>
     */
    private void displayUsernameFields() {
        FieldRelatedLabel usernameLabel = new FieldRelatedLabel(USERNAME_LABEL_TEXT, USERNAME_LABEL_X_POS, USERNAME_LABEL_Y_POS);

        usernameTF = new TextField();
        usernameTF.relocate(USERNAME_TF_X_POS, USERNAME_TF_Y_POS);

        sceneNodes.getChildren().addAll(usernameLabel, usernameTF);
    }

    /**
     * Display the Node related to the Password: the <code>FieldRelatedLabel</code> and the <code>FieldRelatedLabel</code>
     */
    private void displayPasswordFields() {
        FieldRelatedLabel passwordLabel = new FieldRelatedLabel(PASSWORD_LABEL_TEXT, PASSWORD_LABEL_X_POS, PASSWORD_LABEL_Y_POS);

        passwordTF = new PasswordField();
        passwordTF.relocate(PASSWORD_TF_X_POS, PASSWORD_TF_Y_POS);

        sceneNodes.getChildren().addAll(passwordLabel, passwordTF);
    }

    ////////////////////////  BUTTONS  ////////////////////////

    /**
     * Add to the scene the <code>RegularButton</code> for the log in.
     */
    private void displayLogInButton() {
        RegularButton logInButton = new RegularButton(LOGIN_BUTTON_TEXT, LOGIN_BUTTON_X_POS, LOGIN_BUTTON_Y_POS);

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

        if(result) {
            CurrentUI.setUser(user);

            // LOAD THE CHATS
            AuthGateway authGateway = AuthGateway.getInstance();
            user.setChatList(authGateway.getChatHistory(user));

            CurrentUI.changeScene(SceneNames.HOMEPAGE);
        } else {
            invalidFormEntryLabel.setText(RESULT_LABEL_NOTCOR);
            invalidFormEntryLabel.setStyle(RESULT_LABEL_STYLE_RED);
            invalidFormEntryLabel.setVisible(true);
        }
    }

    private void displayRegisterButton() {
        RegularButton registerButton = new RegularButton(REGISTER_BUTTON_TEXT, REGISTER_BUTTON_X_POS, REGISTER_BUTTON_Y_POS);
        registerButton.setOnAction(e -> registerButtonAction());

        sceneNodes.getChildren().add(registerButton);
    }

    private void registerButtonAction() {
        Authenticator auth = AuthGateway.getInstance();
        User user = new User(usernameTF.getText(), passwordTF.getText());
        boolean result = auth.register(user);

        if (result) {
            invalidFormEntryLabel.setText(RESULT_LABEL_SUCCESS);
            invalidFormEntryLabel.setStyle(RESULT_LABEL_STYLE_GREEN);
        } else {
            invalidFormEntryLabel.setText(RESULT_LABEL_TAKEN);
            invalidFormEntryLabel.setStyle(RESULT_LABEL_STYLE_RED);
        }
        invalidFormEntryLabel.setVisible(true);
    }
}
