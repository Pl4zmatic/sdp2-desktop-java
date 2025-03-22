package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import domein.notification.Notification;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
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

	private NotificationService notificationService;
	private NotificationElement notificationElement;

    @FXML
	public void initialize() {
		rootLayout.setLeft(NavbarManager.getNavbar());
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

		for(int i = 0; i < notifications.size(); i++) {
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


		notificationScrollable.setContent(notificationVbox);

		notificationScrollable.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
	}
	
    @FXML
	public void deleteNotification() {
		
	}

    public void showNotificationDetails(Node node) {
        rootLayout.setRight(node);
    }
}
