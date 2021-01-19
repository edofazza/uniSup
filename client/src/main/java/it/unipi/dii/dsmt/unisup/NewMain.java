package it.unipi.dii.dsmt.unisup;

import it.unipi.dii.dsmt.unisup.beans.User;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;

public class NewMain extends Application {
    private static Stage guiStage;
    private static User userLogged;

    private static final Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    public final static int DEF_FRAME_WIDTH = 950;
    public final static int DEF_FRAME_HEIGHT = 600;
    public final static int LOG_FRAME_WIDTH = 420;
    public final static int LOG_FRAME_HEIGHT = 522;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
         guiStage = stage;
        stage.setResizable(false);
        stage.setTitle("UniSup");
        stage.getIcons().add(new javafx.scene.image.Image("/images/logo.png"));
        Scene scene = new Scene(loadFXML("LoginFrame"));
        stage.setScene(scene);
        stage.show();
    }

    public static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(NewMain.class.getResource("/"+ fxml + ".fxml"));

        if(fxml.equalsIgnoreCase("LoginFrame")){
            guiStage.setX(dim.width/2 - LOG_FRAME_WIDTH / 2);
            guiStage.setY(dim.height/2 - LOG_FRAME_HEIGHT / 2);
        }else {
            guiStage.setX(dim.width / 2 - DEF_FRAME_WIDTH / 2);
            guiStage.setY(dim.height / 2 - DEF_FRAME_HEIGHT / 2);
        }
        return fxmlLoader.load();
    }

    public static Stage getStage() {
        return guiStage;
    }

    public static void changeStage(String sceneToLoad) {
        try {
            guiStage.setScene(new Scene(NewMain.loadFXML(sceneToLoad)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setUser(User user) {
        userLogged = user;
    }

    public static User getUserLogged() {
        return userLogged;
    }
}
