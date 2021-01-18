package it.unipi.dii.dsmt.unisup.controller;

import it.unipi.dii.dsmt.unisup.NewMain;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;

import javax.xml.soap.Text;
import java.io.IOException;

public class MainFrameController {
    @FXML
    private Button sendBtn;
    @FXML
    private Button newMessageBtn;
    @FXML
    private Button logoutBtn;
    @FXML
    private ListView messagesList;
    @FXML
    private ListView historyList;
    @FXML
    private TextArea messageTextArea;


    @FXML
    private void initialize() {
        setActionCommands();
    }

    private void setActionCommands() {
        logoutBtn.setOnAction(e ->{
                try {
                    NewMain.getStage().setScene(new Scene(NewMain.loadFXML("LoginFrame")));
                } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        newMessageBtn.setOnAction(event ->{
            final Stage dialog;
            try {
                dialog = new Stage();
                dialog.setResizable(false);
                dialog.setTitle("UniSup");
                dialog.getIcons().add(new javafx.scene.image.Image("/images/logo.png"));
                dialog.initModality(Modality.APPLICATION_MODAL);
                dialog.initOwner(NewMain.getStage());
                Scene dialogScene = new Scene(NewMain.loadFXML("SendMessage"));
                dialog.setScene(dialogScene);
                dialog.show();
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
        sendBtn.setOnAction(e ->{

        });
    }
}
