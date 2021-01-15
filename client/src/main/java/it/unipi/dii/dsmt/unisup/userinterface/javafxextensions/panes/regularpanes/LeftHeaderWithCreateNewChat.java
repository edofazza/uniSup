package it.unipi.dii.dsmt.unisup.userinterface.javafxextensions.panes.regularpanes;

import it.unipi.dii.dsmt.unisup.communication.AuthGateway;
import it.unipi.dii.dsmt.unisup.communication.Authenticator;
import it.unipi.dii.dsmt.unisup.userinterface.CurrentUI;
import it.unipi.dii.dsmt.unisup.userinterface.enumui.SceneNames;
import it.unipi.dii.dsmt.unisup.userinterface.javafxextensions.buttons.LogOutButton;
import it.unipi.dii.dsmt.unisup.userinterface.javafxextensions.buttons.WriteButton;
import it.unipi.dii.dsmt.unisup.userinterface.javafxextensions.group.NewMessageContactGroup;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class LeftHeaderWithCreateNewChat extends Pane {
    private final String LEFT_HEADER_STYLE = "-fx-background-color: #2b3033;";

    private final int WRITE_BUTTON_X_POS = 20;
    private final int WRITE_BUTTON_Y_POS = 0;

    private final double NEW_SCENE_WIDTH = 400;
    private final double NEW_SCENE_HEIGHT = 260;
    private final String STYLE_LOCATION = "file:client/css/UniSup.css";
    private final String NEW_SCENE_TITLE = "UniSup";

    private final String LOGOUT_TEXT = "LOG OUT";
    private  final int LOGOUT_X_POS = 100;
    private final int LOGOUT_Y_POS = 5;

    public LeftHeaderWithCreateNewChat(int x, int y, int width, int height) {
        relocate(x, y);
        setPrefSize(width, height);
        setStyle(LEFT_HEADER_STYLE);

        displayWriteButton();
        displayLogOutButton();
    }

    private void displayWriteButton() {
        WriteButton writeButton = new WriteButton(WRITE_BUTTON_X_POS, WRITE_BUTTON_Y_POS);
        writeButton.setOnAction(e -> writeButtonAction());

        getChildren().add(writeButton);
    }

    private void writeButtonAction() {
        Scene scene = new Scene(new NewMessageContactGroup(), NEW_SCENE_WIDTH, NEW_SCENE_HEIGHT);
        scene.getStylesheets().add(STYLE_LOCATION);

        Stage secondaryStage = new Stage();
        secondaryStage.setTitle(NEW_SCENE_TITLE);
        secondaryStage.setScene(scene);
        secondaryStage.show();
    }

    private void displayLogOutButton() {
        LogOutButton logOutButton = new LogOutButton(LOGOUT_TEXT, LOGOUT_X_POS, LOGOUT_Y_POS);
        logOutButton.setOnAction(e -> logOutButtonAction() );

        getChildren().add(logOutButton);
    }

    private void logOutButtonAction() {
        Authenticator au = AuthGateway.getInstance();
        au.logout(CurrentUI.getUser());
        CurrentUI.changeScene(SceneNames.LOGIN);
        CurrentUI.userExit();
    }
}
