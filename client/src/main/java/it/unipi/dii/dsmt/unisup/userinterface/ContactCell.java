package it.unipi.dii.dsmt.unisup.userinterface;

import it.unipi.dii.dsmt.unisup.Main;
import it.unipi.dii.dsmt.unisup.beans.Chat;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;

public class ContactCell extends ListCell<Chat> {
    VBox vbox = new VBox();
    Label senderLabel = new Label();
    Label lastMsgLabel = new Label();
    Chat lastItem;
    HBox hBox = new HBox();
    StackPane stackPane = new StackPane();
    Circle circle = new Circle();
    Text circleText = new Text();
    public ContactCell() {
        super();
        senderLabel.setPrefWidth(150);
        senderLabel.setPadding(new Insets(10, 10, 5, 10));
        circle.setFill(Color.DARKGRAY);
        circle.setRadius(23.0);
        circle.setStroke(Color.DARKGRAY);
        circle.setStrokeType(StrokeType.INSIDE);
        circle.setVisible(false);

        circleText.setFont(new Font(14));
        circleText.setFill(Color.WHITE);
        circleText.setBoundsType(TextBoundsType.VISUAL);
        stackPane.getChildren().addAll(circle, circleText);

        hBox.getChildren().addAll(senderLabel, stackPane);

        lastMsgLabel.setPadding(new Insets(0, 20, 5, 10));
        lastMsgLabel.setMaxWidth(180);
        senderLabel.setFont(new Font(25));
        lastMsgLabel.setFont(new Font(14));
        lastMsgLabel.setWrapText(true);
        vbox.getChildren().addAll(hBox, lastMsgLabel);

    }

    @Override
    protected void updateItem(Chat item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
            lastItem = null;
            senderLabel.setText("");
            lastMsgLabel.setText("");
            circleText.setText("");
            circle.setVisible(false);
            setGraphic(null);
        } else {
            Text usernameContact = new Text(item.getUsernameContact());
            usernameContact.setFont(senderLabel.getFont());
            Text lastMsg = new Text(item.getHistory().get(item.getHistory().size() - 1).getText());
            lastMsg.setFont(lastMsgLabel.getFont());
            double padding = 20;
            senderLabel.setMaxHeight(usernameContact.getLayoutBounds().getHeight() + 10);
            lastMsgLabel.setMaxHeight(usernameContact.getLayoutBounds().getHeight() + padding);
            senderLabel.setText(usernameContact.getText());
            lastMsgLabel.setText(lastMsg.getText());
            if (item.getUnreadMessages() > 0) {
                circleText.setText(item.getUnreadMessages()+"");
                circle.setVisible(true);
            }else{
                circleText.setText("");
                circle.setVisible(false);
            }
            if(Main.getUserLogged().getUsername().equals(item.getHistory().get(item.getHistory().size() - 1).getSender())) {
                lastMsgLabel.setText("You: "+lastMsgLabel.getText());
            }else{
                lastMsgLabel.setText(lastMsgLabel.getText());
            }
            setGraphic(vbox);
        }
    }
}
