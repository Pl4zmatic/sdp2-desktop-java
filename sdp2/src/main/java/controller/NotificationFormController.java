package controller;

import utils.NotificationType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import domein.notification.Notification;
import domein.user.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import lombok.Setter;
import service.NotificationService;
import service.UserService;

public class NotificationFormController {

  //parent
  @Setter
  private ManageNotificationsController parentController;

  // layout
  @FXML
  private BorderPane rootLayout;
  @FXML
  private Label formTitle;
  @FXML
  private VBox contentVBox;

  // text fields
  @FXML
  private TextField title;
  @FXML
  private TextArea messageTextArea;

  // types
  @FXML
  private RadioButton maintenance;
  @FXML
  private RadioButton reminder;
  @FXML
  private ToggleGroup toggleGroupType;

  // people selection
  @FXML
  private HBox hboxSelectedPeople;
  @FXML
  private ComboBox<User> comboBoxPeople;

  // buttons
  @FXML
  private Button cancel;
  @FXML
  private Button save;

  // class vars
  private Notification notification;
  private NotificationService notificationService;
  private UserService userService;
  private List<User> selectedUserList = new ArrayList<>();
  private List<User> allActiveUsers = new ArrayList<>();

  public void setNotification(Notification notification) {
    this.notification = notification;
    selectedUserList = new ArrayList<>(notificationService.getAllUsersByNotification(this.notification));
    allActiveUsers.removeAll(selectedUserList);
    comboBoxPeople.setItems(FXCollections.observableArrayList(allActiveUsers));
    loadContentSelectedPeople();
    setControls();
  }

  @FXML
  private void initialize() {
    this.notificationService = NotificationService.getInstance();
    this.userService = UserService.getInstance();
    this.allActiveUsers = new ArrayList<>(userService.getAllActiveUsers());
    setupComboBox();
    setupCallbacks();
  }

  private void setupCallbacks() {
    title.focusedProperty().addListener((event) -> checkTextField(title, "Title is mandatory."));
    messageTextArea.focusedProperty().addListener((event) -> checkTextField(messageTextArea, "Message is mandatory."));
    toggleGroupType.selectedToggleProperty().addListener((event) -> checkToggleGroup(toggleGroupType));
    save.setOnAction((event) -> addOrSave());
    cancel.setOnAction(event -> parentController.removeForm());

    // combo box
    comboBoxPeople.getEditor().textProperty().addListener((obs, oldValue, newValue) -> filterComboBoxByName(newValue));
    comboBoxPeople.setOnHidden(event -> {
      if (comboBoxPeople.getSelectionModel().getSelectedItem() instanceof User) {
        selectUser(comboBoxPeople.getSelectionModel().getSelectedItem());
        loadContentSelectedPeople();
      }
    });
  }

  private void setupComboBox() {
    ObservableList<User> observableUsers = FXCollections.observableArrayList(allActiveUsers);
    comboBoxPeople.setItems(observableUsers);

    comboBoxPeople.setCellFactory(new Callback<ListView<User>, ListCell<User>>() {
      @Override
      public ListCell<User> call(ListView<User> listView) {
        return new ListCell<User>() {
          @Override
          protected void updateItem(User item, boolean empty) {
            super.updateItem(item, empty);
            if (item != null && !empty)
              setText(item.getFullName());
          }
        };
      }
    });
  }

  private void filterComboBoxByName(String filter) {
    ObservableList<User> ObservableUsers = FXCollections.observableArrayList(allActiveUsers);
    FilteredList<User> filteredUsers = new FilteredList<>(ObservableUsers, item -> true);
    filteredUsers.setPredicate((user) -> {
      if (filter.isBlank() || filter.isEmpty())
        return true;
      return user.getFirstName().toLowerCase().contains(filter.toLowerCase())
          || user.getLastName().toLowerCase().contains(filter.toLowerCase());
    });

    if (!filteredUsers.isEmpty())
      comboBoxPeople.setItems(filteredUsers);

    comboBoxPeople.show();
  }

  private void selectUser(User user) {
    this.allActiveUsers.remove(user);
    this.selectedUserList.add(user);
    comboBoxPeople.setItems(FXCollections.observableArrayList(allActiveUsers));
    comboBoxPeople.show();
  }

  private void loadContentSelectedPeople() {
    hboxSelectedPeople.getChildren().clear();
    for (User user : selectedUserList) {

      // userContainer
      HBox hbox = new HBox(0);

      // buttons
      Button button = new Button(user.getFullName());
      ImageView buttonImage = new ImageView(new Image(getClass().getResourceAsStream("/images/User_fill.png")));

      Button closeButton = new Button();
      ImageView closeImage = new ImageView(new Image(getClass().getResourceAsStream("/images/close.png")));
      // styling
      {
        hbox.setMaxWidth(125);
        hbox.setAlignment(Pos.CENTER_LEFT);

        buttonImage.setFitWidth(20);
        buttonImage.setFitHeight(20);
        button.setPadding(new Insets(0));
        button.setGraphic(buttonImage);

        closeImage.setFitWidth(20);
        closeImage.setFitHeight(20);
        closeButton.setPadding(new Insets(0));
        closeButton.setGraphic(closeImage);
      }

      // hou user object bij
      hbox.setUserData(user);

      // callback
      closeButton.setOnAction((event) -> unselectUser((User) hbox.getUserData()));

      hbox.getChildren().add(button);
      hbox.getChildren().add(closeButton);

      hboxSelectedPeople.getChildren().add(hbox);
    }
  }

  private void unselectUser(User user) {
    selectedUserList.remove(user);
    allActiveUsers.add(user);
    comboBoxPeople.setItems(FXCollections.observableArrayList(allActiveUsers));
    loadContentSelectedPeople();
  }

  private void setControls() {
    title.setText(notification.getTitle());
    messageTextArea.setText(notification.getMessage());

    switch (notification.getType()) {
      case NotificationType.MAINTENANCE:
        toggleGroupType.selectToggle(maintenance);
        break;

      case NotificationType.REMINDER:
        toggleGroupType.selectToggle(reminder);
        break;
    }

    if (this.notification == null)
      save.setText("Add");
    else
      save.setText("Save");
  }

  private <T extends TextInputControl> boolean checkTextField(T source, String error) {
    if ((source.getText().isBlank() || source.getText().isEmpty()) && !source.isFocused()) {
      source.setPromptText(error);
      return true;
    }

    return false;
  }

  private boolean checkToggleGroup(ToggleGroup t) {
    if (t.getSelectedToggle() == null) {
      reminder.setStyle("-fx-text-fill: -bgRed; -fx-font-weight: bold;");
      maintenance.setStyle("-fx-text-fill: -bgRed; -fx-font-weight: bold;");
      return true;
    }

    reminder.setStyle("-fx-text-fill: -deepBlue");
    maintenance.setStyle("-fx-text-fill: -deepBlue");
    return false;
  }

  private boolean checkAll() {
    if (Arrays.asList(
        checkTextField(title, "Title is mandatory."),
        checkTextField(messageTextArea, "Message is mandatory."),
        checkToggleGroup(toggleGroupType)).contains(true)) {
      return true;
    }

    return false;
  }

  private void setNotification() {
    if (notification == null)
      this.notification = new Notification();

    notification.setTitle(title.getText());
    notification.setMessage(messageTextArea.getText());
    notification.setType(toggleGroupType.getSelectedToggle().equals(reminder) ? NotificationType.REMINDER
        : NotificationType.MAINTENANCE);
  }

  private void addOrSave() {
    if (!checkAll()) {
      if (notification != null) {
        setNotification();
        notificationService.updateNotification(notification, this.selectedUserList);
      } else {
        setNotification();
        notificationService.createNotification(notification, this.selectedUserList);
      }
      parentController.loadTableContent();
      parentController.selectNotificationInTable(notification);
      parentController.removeForm();
    }
  }
}
