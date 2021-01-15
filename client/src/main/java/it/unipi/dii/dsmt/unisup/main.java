package it.unipi.dii.dsmt.unisup;

import it.unipi.dii.dsmt.unisup.userinterface.CurrentUI;
import javafx.application.*;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class main extends Application {
    private final String TITLE = "UniSup";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Scene scene = new CurrentUI().initScene();
        
        primaryStage.setTitle(TITLE);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
