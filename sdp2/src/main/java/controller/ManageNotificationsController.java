package controller;

import java.io.IOException;
import domein.notification.Notification;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;
import service.ServiceController;
import utils.NotificationType;

public class ManageNotificationsController {

  @FXML
  private Button addButton;

  @FXML
  private StackPane rightPanelContainer;

  @FXML
  private BorderPane rootLayout;

  @FXML
  private TextField searchBar;

  @FXML
  private HBox searchBarContainer;

  @FXML
  private TableView<Notification> tableView;

  @FXML
  private ComboBox<String> typeFilterComboBox;

  private ServiceController serviceController;
  private FilteredList<Notification> notifications;

  @FXML
  private void initialize() throws IOException {
    serviceController = ServiceController.getInstance();

    setupNavbar();
    setupTable();
    setupCallbacks();
    setupFilters();
  }

  private void setupNavbar() throws IOException {
    this.rootLayout.setLeft(NavbarManager.getNavbar());
  }

  private void setupCallbacks() {
    tableView.setOnMouseClicked(event -> {
      if (event.getClickCount() == 2) {
        Notification selected = tableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
          addOrEditNotification(selected);
        }
      }
    });

    addButton.setOnAction((event) -> addOrEditNotification(null));
    typeFilterComboBox.setOnHiding(event -> {
      typeFilterComboBox.getEditor().setText(typeFilterComboBox.getSelectionModel().getSelectedItem());
      applyFilters();
    });
    searchBar.textProperty().addListener(event -> applyFilters());
  }

  private void setupFilters() {
    typeFilterComboBox
        .setItems(FXCollections.observableArrayList("All types", NotificationType.MAINTENANCE.name(),
            NotificationType.REMINDER.name()));
    typeFilterComboBox.getSelectionModel().select(0);
  }

  private void applyFilters() {
    NotificationType filteredType = NotificationType
        .getTypeByString(typeFilterComboBox.getSelectionModel().getSelectedItem());
    String searchString = searchBar.getText();

    notifications.setPredicate(notif -> {
      boolean typeCheck, stringCheck;
      if (filteredType != null)
        typeCheck = notif.getType().equals(filteredType);
      else typeCheck = true;
      stringCheck = notif.getTitle().toLowerCase().contains(searchString.toLowerCase())
          || notif.getMessage().toLowerCase().contains(searchString.toLowerCase());
      return typeCheck && stringCheck;
    });
  }

  private void setupTable() {
    TableColumn<Notification, String> titleColumn = new TableColumn<>("Title");
    TableColumn<Notification, String> messageColumn = new TableColumn<>("Message");
    TableColumn<Notification, String> typeColumn = new TableColumn<>("Type");
    TableColumn<Notification, Void> actionsColumn = new TableColumn<>("Actions");

    titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
    messageColumn.setCellValueFactory(new PropertyValueFactory<>("message"));
    typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));

    // styling
    titleColumn.setPrefWidth(175);
    messageColumn.setPrefWidth(400);

    actionsColumn.setCellFactory(new Callback<TableColumn<Notification, Void>, TableCell<Notification, Void>>() {
      @Override
      public TableCell<Notification, Void> call(final TableColumn<Notification, Void> param) {
        return new TableCell<Notification, Void>() {
          private final HBox hbox = new HBox(10);
          private final Button editButton = new Button("Edit");
          private final Button deleteButton = new Button("Delete");

          {
            hbox.setAlignment(Pos.CENTER);
            hbox.getStyleClass().add("actions-container");
            hbox.setMinHeight(40);
            hbox.setPrefHeight(40);

            editButton.getStyleClass().add("edit-button");
            editButton.setMaxWidth(Double.MAX_VALUE);
            editButton.setMinHeight(30);
            editButton.setPrefHeight(30);
            HBox.setHgrow(editButton, Priority.ALWAYS);

            deleteButton.getStyleClass().add("delete-button");
            deleteButton.setMaxWidth(Double.MAX_VALUE);
            deleteButton.setMinHeight(30);
            deleteButton.setPrefHeight(30);
            HBox.setHgrow(deleteButton, Priority.ALWAYS);

            hbox.getChildren().addAll(editButton, deleteButton);
          }

          @Override
          protected void updateItem(Void item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
              setGraphic(null);
            } else {
              setGraphic(hbox);

              // Zorg ervoor dat de knoppen de juiste actie uitvoeren
              Notification notification = getTableView().getItems().get(getIndex());

              editButton.setOnAction(event -> {
                addOrEditNotification(notification);
              });

              deleteButton.setOnAction(event -> {
                deleteNotification(notification);
              });
            }
          }
        };
      }
    });

    tableView.getColumns().addAll(typeColumn, titleColumn, messageColumn, actionsColumn);
    loadTableContent();
  }

  protected void loadTableContent() {
    if (notifications != null) {
      notifications.getSource().clear();
    }
    notifications = new FilteredList<>(FXCollections.observableArrayList(serviceController.getAllNotifications()));

    tableView.getItems().clear();
    tableView.setItems(notifications);
  }

  private void deleteNotification(Notification notification) {
    serviceController.deleteNotification(notification);
    loadTableContent();
  }

  private void addOrEditNotification(Notification notification) {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/NotificationForm.fxml"));
      rootLayout.setRight(loader.load());
      NotificationFormController controller = loader.getController();
      controller.setParentController(this);

      if (notification != null) {
        controller.setNotification(notification);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  protected void selectNotificationInTable(Notification notification) {
    tableView.getSelectionModel().select(notification);
  }

  protected void removeForm() {
    rootLayout.setRight(null);
  }
}
