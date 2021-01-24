package it.unipi.dii.dsmt.unisup.userinterface;

import it.unipi.dii.dsmt.unisup.Main;
import it.unipi.dii.dsmt.unisup.beans.Message;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class MessageCell extends ListCell<Message> {
    VBox vbox = new VBox();
    Label senderLabel = new Label();
    TextArea textArea = new TextArea();
    Message lastItem;
    public MessageCell() {
        super();
        senderLabel.setPrefSize(330, 33);
        senderLabel.setPadding(new Insets(10,20,5,10));
        senderLabel.setFont(new Font(19));
        senderLabel.setText("Sender");
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
        vbox.getChildren().addAll(senderLabel, textArea);
        VBox.setVgrow(textArea, Priority.NEVER);

    }
    @Override
    protected void updateItem(Message item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            lastItem = null;
            vbox.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
            textArea.setStyle("-fx-control-inner-background:white;");
            textArea.clear();
            senderLabel.setText("");
            setGraphic(null);
        } else {
            senderLabel.setFont(new Font(19));
            Text text = new Text(item.getText());
            text.setFont(textArea.getFont());

            Text timestamp = new Text("\n"+item.getFormattedTimestamp());
            timestamp.setFont(textArea.getFont());
            double padding = 20 ;
            textArea.setMaxWidth(Math.max(text.getLayoutBounds().getWidth(), timestamp.getLayoutBounds().getWidth())+padding);
            textArea.setMaxHeight(text.getLayoutBounds().getHeight()+20+timestamp.getLayoutBounds().getHeight());
            textArea.setText(text.getText() + timestamp.getText());

            senderLabel.setText(item.getSender());
            if(Main.getUserLogged().getUsername().equals(item.getSender())) {
                vbox.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
                textArea.setStyle("-fx-control-inner-background:#73e1f3;");
            }else{
                vbox.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
                textArea.setStyle("-fx-control-inner-background:white;");
            }
            setGraphic(vbox);
        }
    }
}
