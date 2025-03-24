package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import domein.Session;
import domein.notification.Notification;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import service.NotificationService;
import utils.NotificationType;

public class NotificationViewController {
    
    @FXML
    private BorderPane rootLayout;

    @FXML
    private VBox contentVBox;

    @FXML
    private Label formTitle;

    @FXML
    private ScrollPane notificationScrollable;

    @FXML
    private VBox notificationVbox;

    @FXML
    private Button showAllNotificationsButton;

    @FXML
    private Button showLessNotificationsButton;

    @FXML
    private VBox buttonAndNotificationVbox;

	private NotificationService notificationService;
	private NotificationElement notificationElement;

    @FXML
	public void initialize() {
        notificationService = NotificationService.getInstance();
		rootLayout.setLeft(NavbarManager.getNavbar());
		//List<Notification> notifications = notificationService.getAllNotificationsByUser(Session.getCurrentUser());
        List<Notification> notifications = new ArrayList<>();
        notifications.add(new Notification(
                NotificationType.MAINTENANCE,
                "Er staat onderhoud gepland voor 25 maart.",
                "Onderhoud Aankondiging"
        ));

        notifications.add(new Notification(
                NotificationType.REMINDER,
                "Er is een probleem met de server, controleer de status.",
                "Server Waarschuwing"
        ));

        notifications.add(new Notification(
                NotificationType.MAINTENANCE,
                "Nieuwe updates zijn beschikbaar voor de applicatie.",
                "Update Beschikbaar"
        ));

        notifications.add(new Notification(
                NotificationType.MAINTENANCE,
                "Nieuwe updates zijn beschikbaar voor de applicatie.",
                "Update Beschikbaar"
        ));

        notifications.add(new Notification(
                NotificationType.MAINTENANCE,
                "Nieuwe updates zijn beschikbaar voor de applicatie.",
                "Update Beschikbaar"
        ));

        notifications.add(new Notification(
                NotificationType.MAINTENANCE,
                "Nieuwe updates zijn beschikbaar voor de applicatie.",
                "Update Beschikbaar"
        ));

		showFiveNotifications(notifications);


		notificationScrollable.setContent(buttonAndNotificationVbox);

		notificationScrollable.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        showAllNotificationsButton.setOnAction(event -> showAllNotifications(notifications));
        showLessNotificationsButton.setOnAction(event -> showFiveNotifications(notifications));
	}
	
    @FXML
	public void deleteNotification() {
		
	}

    public void showNotificationDetails(Node node) {
        VBox wrapper = new VBox(node);
        wrapper.setAlignment(Pos.CENTER);
        rootLayout.setCenter(wrapper);
        //rootLayout.setCenter(node);
    }

    private void loadFiveNotifications(List<Notification> notifications) {
        loadAllNotifications(notifications, 5);
    }

    private void loadAllNotifications(List<Notification> notifications, int size) {
        notificationVbox.getChildren().removeIf(node -> !(node instanceof Button) && !(node instanceof VBox));
        for(int i = 0; i < size; i++) {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass()
					.getResource("/view/NotificationItem.fxml"));
			Parent element = null;
			try {
				element = fxmlLoader.load();
			} catch (IOException e) {
				e.printStackTrace();
			}
			NotificationElement controller = fxmlLoader.getController();
			controller.fillNotificationElement(notifications.get(i), notificationVbox);

			if(element != null) {
				notificationVbox.getChildren().add(element);
				notificationVbox.setSpacing(8);
			}
		}
    }

    @FXML
    public void showFiveNotifications(List<Notification> notifications) {
        loadFiveNotifications(notifications);
        for(Node node: notificationVbox.getChildren().subList(0, 5)) {
            node.setVisible(true);
            node.setManaged(true);
        }
        for(Node node: notificationVbox.getChildren().subList(5, notificationVbox.getChildren().size())) {
            if(!(node instanceof Button)) {
                node.setVisible(false);
                node.setManaged(false);
            }
        }
        showAllNotificationsButton.setVisible(true);
        showAllNotificationsButton.setManaged(true);
        showLessNotificationsButton.setVisible(false);
        showLessNotificationsButton.setManaged(false);
    }

    @FXML
    public void showAllNotifications(List<Notification> notifications) {
        loadAllNotifications(notifications, notifications.size());
        for(Node node: notificationVbox.getChildren()) {
			node.setVisible(true);
            node.setManaged(true);
		}
        showAllNotificationsButton.setVisible(false);
        showAllNotificationsButton.setManaged(false);
        showLessNotificationsButton.setVisible(true);
        showLessNotificationsButton.setManaged(true);
    }
}
