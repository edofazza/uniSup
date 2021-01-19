package it.unipi.dii.dsmt.unisup.userinterface;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.util.Optional;

public class PopUp {
    public static boolean showPopUpMessage(String title, String header, String context, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(context);
        alert.setResizable(false);
        alert.initOwner(alert.getOwner());

        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.setAlwaysOnTop(true);
        stage.toFront(); // not sure if necessary
        if (!alertType.equals(Alert.AlertType.CONFIRMATION))
            alert.showAndWait();
        else {
            Optional<ButtonType> result = alert.showAndWait();
            if (!result.isPresent())
                return false;
            else if (result.get() == ButtonType.OK)
                return true;
            else if (result.get() == ButtonType.CANCEL)
                return false;
        }
        return false;
    }

}
