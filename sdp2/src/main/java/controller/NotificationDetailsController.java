package controller;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Priority;
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
    @FXML
    private ImageView notificationBackArrow;

    public void fillNotificationElement(String title, String date, String message) {
        VBox.setVgrow(contentVBox, Priority.ALWAYS);
        notificationTitleLabel.setText(title);
        notificationDateLabel.setText(date);
        notificationMessageLabel.setText(message);
        notificationBackArrow.setOnMouseClicked(event -> navToNotificationList());
    }

    @FXML
    void navToNotificationList() {
        SceneSwitcher.switchScene("/view/ShowNotifications.fxml");
    }
    
}
