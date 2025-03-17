package controller;

import domein.user.User;
import service.UserService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;
import utils.Rollen;

import java.io.IOException;

public class GebruikersBeheerderController {

    @FXML private BorderPane rootLayout;
    @FXML private TableView<User> userTable;
    @FXML private TextField searchField;
    @FXML private Button addUserButton;
    @FXML private CheckBox showDeletedUsers;
    @FXML private StackPane rightPanelContainer;
    @FXML private HBox searchBarContainer;

    private ObservableList<User> users;
    private FilteredList<User> filteredUsers;
    private UserService userService = UserService.getInstance();
    private UserFormController formController;

    private void loadUsersFromDatabase() {
        if (users != null) {
            users.clear();
        }

        if(showDeletedUsers.isSelected())
            users = FXCollections.observableArrayList(userService.getAllUsers());
        else
            users = FXCollections.observableArrayList(userService.getAllActiveUsers());

        filteredUsers = new FilteredList<>(users, p -> true);

        userTable.getItems().clear();
        userTable.setItems(filteredUsers);
    }

    @FXML
    public void initialize() {
        rootLayout.setLeft(NavbarManager.getNavbar());

        setupTable();
        loadUsersFromDatabase();
        setupSearch();

        userTable.widthProperty().addListener((obs, oldVal, newVal) -> {
            searchBarContainer.setPrefWidth(newVal.doubleValue());
        });

        addUserButton.setText("+");
        addUserButton.setOnAction(e -> addUser());

        showDeletedUsers.selectedProperty().addListener((observable, oldValue, newValue) -> {
            loadUsersFromDatabase();
        });

        // Dubbelklik om te editten
        userTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                User selectedUser = userTable.getSelectionModel().getSelectedItem();
                if (selectedUser != null) {
                    editUser(selectedUser);
                }
            }
        });
    }

    private void setupTable() {
        userTable.getColumns().clear();

        // Aanmaken kolommen
        TableColumn<User, String> firstNameColumn = new TableColumn<>("First Name");
        TableColumn<User, String> lastNameColumn = new TableColumn<>("Last Name");
        TableColumn<User, String> emailColumn = new TableColumn<>("Email");
        TableColumn<User, String> addressColumn = new TableColumn<>("Address");
        TableColumn<User, Rollen> roleColumn = new TableColumn<>("Role");
        TableColumn<User, Boolean> statusColumn = new TableColumn<>("Status");
        TableColumn<User, Void> actionsColumn = new TableColumn<>("Actions");

        // value instellen
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("adres"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("rol"));

        statusColumn.setCellValueFactory(cellData -> {
            boolean isDeleted = cellData.getValue().getDeleted();
            return javafx.beans.binding.Bindings.createObjectBinding(() -> !isDeleted);
        });
        statusColumn.setCellFactory(column -> new TableCell<User, Boolean>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item ? "Active" : "Inactive");
                }
            }
        });

        firstNameColumn.setPrefWidth(150);
        lastNameColumn.setPrefWidth(150);
        emailColumn.setPrefWidth(220);
        addressColumn.setPrefWidth(180);
        roleColumn.setPrefWidth(120);
        statusColumn.setPrefWidth(120);
        actionsColumn.setPrefWidth(200);
        actionsColumn.setMinWidth(160);

        firstNameColumn.setStyle("-fx-alignment: CENTER-LEFT;");
        lastNameColumn.setStyle("-fx-alignment: CENTER-LEFT;");
        emailColumn.setStyle("-fx-alignment: CENTER-LEFT;");
        addressColumn.setStyle("-fx-alignment: CENTER-LEFT;");
        roleColumn.setStyle("-fx-alignment: CENTER-LEFT;");
        statusColumn.setStyle("-fx-alignment: CENTER-LEFT;");
        actionsColumn.setStyle("-fx-alignment: CENTER;");

        actionsColumn.setCellFactory(new Callback<TableColumn<User, Void>, TableCell<User, Void>>() {
            @Override
            public TableCell<User, Void> call(final TableColumn<User, Void> param) {
                return new TableCell<User, Void>() {
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

                            User user = getTableView().getItems().get(getIndex());

                            editButton.setOnAction(event -> {
                                editUser(user);
                            });

                            deleteButton.setOnAction(event -> {
                                deleteUser(user);
                            });
                        }
                    }
                };
            }
        });
        //Kolommen toevoegen aan tabel
        userTable.getColumns().addAll(
                firstNameColumn, lastNameColumn, emailColumn,
                addressColumn, roleColumn, statusColumn, actionsColumn
        );

        userTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        double totalWidth = firstNameColumn.getPrefWidth() + lastNameColumn.getPrefWidth() +
                emailColumn.getPrefWidth() + addressColumn.getPrefWidth() +
                roleColumn.getPrefWidth() + statusColumn.getPrefWidth() + actionsColumn.getPrefWidth();

        userTable.setPrefWidth(totalWidth + 70);

        searchBarContainer.setPrefWidth(totalWidth + 70);

        userTable.setFixedCellSize(50);

        userTable.getStyleClass().add("user-table");
    }

    private User findUserByEmail(String email) {
        for (User user : users) {
            if (user.getEmail().equals(email)) {
                return user;
            }
        }
        return null;
    }

    private void setupSearch() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredUsers.setPredicate(user -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true; // niks ingegeven --> toon alle users
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return user.getFirstName().toLowerCase().contains(lowerCaseFilter) ||
                        user.getLastName().toLowerCase().contains(lowerCaseFilter);
            });
        });
    }

    private void addUser() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/UserForm.fxml"));
            Parent formRoot = loader.load();

            formController = loader.getController();
            formController.setEditMode(false);

            formController.addCloseButton(event -> hideRightPanel());

            formController.setOnSaveCallback(() -> {
                refreshTable();
                hideRightPanel();
            });

            showRightPanel(formRoot);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void editUser(User user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/UserForm.fxml"));
            Parent formRoot = loader.load();

            formController = loader.getController();
            formController.setEditMode(true);
            formController.setUser(user);

            formController.addCloseButton(event -> hideRightPanel());

            formController.setOnSaveCallback(() -> {
                refreshTable();
                hideRightPanel();
            });

            showRightPanel(formRoot);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showRightPanel(Parent content) {
        rightPanelContainer.getChildren().clear();
        rightPanelContainer.getChildren().add(content);
        rightPanelContainer.setVisible(true);
        rightPanelContainer.setManaged(true);
    }

    private void hideRightPanel() {
        rightPanelContainer.getChildren().clear();
        rightPanelContainer.setVisible(false);
        rightPanelContainer.setManaged(false);
    }

    private void deleteUser(User user) {
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure you want to delete user " + user.getFirstName() + " " + user.getLastName() + "?",
                ButtonType.YES, ButtonType.NO);
        confirmDialog.setTitle("Confirm Delete");
        confirmDialog.setHeaderText("Delete User");

        confirmDialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                boolean success = userService.deleteUser(user.getEmail());
                if (success) {
                    refreshTable();
                    hideRightPanel();
                } else {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR,
                            "Failed to delete user with email: " + user.getEmail(),
                            ButtonType.OK);
                    errorAlert.setTitle("Error");
                    errorAlert.setHeaderText("Delete Failed");
                    errorAlert.showAndWait();
                }
            }
        });
    }

    public void refreshTable() {
        loadUsersFromDatabase();
    }
}

