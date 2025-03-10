package controller;

import domein.user.User;
import domein.user.UserService;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import utils.Rollen;

import java.io.IOException;
import java.util.Comparator;
import java.util.function.Function;

public class GebruikersBeheerderController {

    @FXML private BorderPane rootLayout;
    @FXML private MFXTableView<User> userTable;
    @FXML private TextField searchField;
    @FXML private Button addUserButton;
    @FXML private CheckBox showDeletedUsers;

    private ObservableList<User> users;
    private FilteredList<User> filteredUsers;
    private UserService userService = UserService.getInstance();

    private void loadUsersFromDatabase() {
        if (users != null) {
            users.clear();
        }

        if(showDeletedUsers.isSelected())
            users = FXCollections.observableArrayList(userService.getAllUsers());
        else
            users = FXCollections.observableArrayList(userService.getAllActiveUsers());

        filteredUsers = new FilteredList<>(users, p -> true);

        // Clear and reset the table
        userTable.getItems().clear();
        userTable.setItems(filteredUsers);
    }

    @FXML
    public void initialize() {
        rootLayout.setLeft(NavbarManager.getNavbar());

        setupTable();
        loadUsersFromDatabase();
        setupSearch();

        addUserButton.setText("+");
        addUserButton.setOnAction(e -> addUser());

        showDeletedUsers.selectedProperty().addListener((observable, oldValue, newValue) -> {
            loadUsersFromDatabase();
        });
    }

    private void setupTable() {
        userTable.getTableColumns().clear();

        // Kolommen voor User-attributen
        MFXTableColumn<User> firstNameColumn = new MFXTableColumn<>("First Name", true, Comparator.comparing(User::getFirstName));
        MFXTableColumn<User> lastNameColumn = new MFXTableColumn<>("Last Name", true, Comparator.comparing(User::getLastName));
        MFXTableColumn<User> emailColumn = new MFXTableColumn<>("Email", true, Comparator.comparing(User::getEmail));
        MFXTableColumn<User> addressColumn = new MFXTableColumn<>("Address", true, Comparator.comparing(User::getAdres));
        MFXTableColumn<User> roleColumn = new MFXTableColumn<>("Role", true, Comparator.comparing(User::getRol));
        MFXTableColumn<User> statusColumn = new MFXTableColumn<>("Status", true, Comparator.comparing(user -> !user.getDeleted()));
        MFXTableColumn<User> actionsColumn = new MFXTableColumn<>("Actions", true);

        // Stel table cells in
        firstNameColumn.setRowCellFactory(user -> new MFXTableRowCell<>(User::getFirstName));
        lastNameColumn.setRowCellFactory(user -> new MFXTableRowCell<>(User::getLastName));
        emailColumn.setRowCellFactory(user -> new MFXTableRowCell<>(User::getEmail));
        addressColumn.setRowCellFactory(user -> new MFXTableRowCell<>(User::getAdres));
        roleColumn.setRowCellFactory(user -> new MFXTableRowCell<>(User::getRol));
        statusColumn.setRowCellFactory(user -> new MFXTableRowCell<>(u -> u.getDeleted() ? "Inactive" : "Active"));

        actionsColumn.setRowCellFactory(user -> {
            MFXTableRowCell<User, String> cell = new MFXTableRowCell<>(u -> "");

            final String userEmail = user.getEmail();
            final Rollen userRole = user.getRol();
            final boolean isDeleted = user.getDeleted();

            HBox hbox = new HBox(10);
            hbox.setAlignment(Pos.CENTER);
            hbox.getStyleClass().add("actions-container");
            hbox.setMinHeight(40);
            hbox.setPrefHeight(40);

            Button editButton = new Button("Edit");
            editButton.getStyleClass().add("edit-button");
            editButton.setMaxWidth(Double.MAX_VALUE);
            editButton.setOnAction(event -> {
                User selectedUser = findUserByEmail(userEmail);
                if (selectedUser != null) {
                    editUser(selectedUser);
                } else {
                    System.out.println("ERROR: Could not find user with email: " + userEmail);
                }
            });

            Button deleteButton = new Button("Delete");
            deleteButton.getStyleClass().add("delete-button");
            deleteButton.setMaxWidth(Double.MAX_VALUE);
            deleteButton.setOnAction(event -> {
                User selectedUser = findUserByEmail(userEmail);
                if (selectedUser != null) {
                    deleteUser(selectedUser);
                } else {
                    System.out.println("ERROR: Could not find user with email: " + userEmail);
                }
            });

            hbox.getChildren().addAll(editButton, deleteButton);
            cell.setGraphic(hbox);
            cell.setAlignment(Pos.CENTER);

            return cell;
        });

        actionsColumn.setPrefWidth(160);
        actionsColumn.setMinWidth(160);

        userTable.getTableColumns().addAll(firstNameColumn, lastNameColumn, emailColumn, addressColumn, roleColumn, statusColumn, actionsColumn);

        userTable.setFooterVisible(false);
        searchField.setPromptText("Search name...");
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

    private void addUser(){
        try {
            SceneSwitcher.switchScene("/view/UserForm.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteUser(User user) {
        boolean success = userService.deleteUser(user.getEmail());
        if (success) {
            loadUsersFromDatabase();
        } else {
            System.out.println("Failed to delete user with email: " + user.getEmail());
        }
    }

    private void editUser(User user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/UserForm.fxml"));
            Parent root = loader.load();

            UserFormController controller = loader.getController();
            controller.setEditMode(true);
            controller.setUser(user);

            // In plaats van switchScene, direct de root zetten
            Scene scene = new Scene(root);
            SceneSwitcher.getStage().setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

