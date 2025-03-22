package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class NotificationDetailsController {
    @FXML
    private Label notificationTitleLabel;
    @FXML
    private Label notificationDateLabel;
    @FXML
    private Label notificationMessageLabel;
    @FXML
    private VBox contentVBox;

    public void fillNotificationElement(String title, String date, String message) {
        contentVBox.setPrefWidth(1000);
        notificationTitleLabel.setText(title);
        notificationDateLabel.setText(date);
        notificationMessageLabel.setText(message);
    }
    
}
