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
    public LeftHeaderWithCreateNewChat(int x, int y, int width, int height) {
        relocate(x, y);
        setPrefSize(width, height);
        setStyle("-fx-background-color: #2b3033;");

        displayWriteButton();
        displayLogOutButton();
    }

    private void displayWriteButton() {
        WriteButton writeButton = new WriteButton(20, 0);
        writeButton.setOnAction(e -> writeButtonAction());

        getChildren().add(writeButton);
    }

    private void writeButtonAction() {
        Scene scene = new Scene(new NewMessageContactGroup(), 400, 260);
        scene.getStylesheets().add("file:client/css/UniSup.css");

        Stage secondaryStage = new Stage();
        secondaryStage.setTitle("UniSup");
        secondaryStage.setScene(scene);
        secondaryStage.show();
    }

    private void displayLogOutButton() {
        LogOutButton logOutButton = new LogOutButton("LOG OUT", 100, 5);
        logOutButton.setOnAction(e -> logOutButtonAction() );

        getChildren().add(logOutButton);
    }

    private void logOutButtonAction() {
        Authenticator au = AuthGateway.getInstance();
        au.logout(CurrentUI.getUser());
        CurrentUI.changeScene(SceneNames.LOGIN);
        CurrentUI.setUser(null);
    }
}
