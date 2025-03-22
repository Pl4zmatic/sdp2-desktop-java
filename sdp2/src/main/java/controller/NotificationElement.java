package controller;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

import domein.notification.Notification;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import service.NotificationService;
import utils.NotificationType;
    
public class NotificationElement {
    
    @FXML
    private Button notificationDeleteButton;
    
    @FXML
    private Label notificationDateLabel;
    
    @FXML
    private ImageView notificationDeleteImage;
    
    @FXML
    private Label notificationStatusLabel;
    
    @FXML
    private Label notificationTitleLabel;
    
    @FXML
    private ImageView notificationTypeImage;

    @FXML
    private HBox notificationItemHbox;

    private NotificationService notificationService;
    private String message;
        
    @FXML
    public void initialize() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/NotificationItem.fxml"));
        
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        if(notificationDeleteButton != null) {
        }
    }
    
    public void fillNotificationElement(Notification notification, VBox parentContainer) {
        notificationService = NotificationService.getInstance();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String date = notification.getDateAndTime().format(dateFormat);
        notificationDateLabel.setText(date);
        notificationStatusLabel.setText("Nieuw");
        notificationTitleLabel.setText(notification.getTitle());
        if(notification.getType() == NotificationType.MAINTENANCE) {
            notificationTypeImage.setImage(new Image("/images/maintenance-icon-black.png"));
        } else if(notification.getType() == NotificationType.REMINDER) {
            notificationTypeImage.setImage(new Image("/images/maintenance-icon.png"));
        }
        message = notification.getMessage();
        notificationDeleteButton.setOnAction(event -> parentContainer.getChildren().remove(notificationItemHbox));
        notificationItemHbox.setOnMouseClicked(event -> showNotificationDetails());
    }

    @FXML
    public void deleteNotification(Notification notification) {
        notificationService.deleteNotification(notification);
    }
    @FXML
    public void showNotificationDetails() {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/NotificationDetails.fxml"));
            //VBox root = fxmlLoader.load();
            
            /*Stage popUpStage = new Stage();
            popUpStage.initModality(Modality.APPLICATION_MODAL);
            
            Scene scene = new Scene(root);
            popUpStage.setScene(scene);
            popUpStage.showAndWait();*/
            Parent notificationDetailsBox = fxmlLoader.load();
            NotificationDetailsController controller = fxmlLoader.getController();
            controller.fillNotificationElement(notificationTitleLabel.getText(), notificationDateLabel.getText(), message);
            NotificationViewController parentController = SceneSwitcher.getCurrentController();
            parentController.showNotificationDetails(notificationDetailsBox);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
    
