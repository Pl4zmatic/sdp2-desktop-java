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
import javafx.scene.control.CheckBox;
import javafx.scene.layout.HBox;
import java.io.IOException;
import java.util.Comparator;

public class GebruikersBeheerderController {

    @FXML private HBox rootLayout;
    @FXML private MFXTableView<User> userTable;
    @FXML private MFXTextField searchField;
    @FXML private MFXButton addUserButton;
    @FXML private CheckBox showDeletedUsers;

    private ObservableList<User> users;
    private FilteredList<User> filteredUsers;
    private UserService userService = UserService.getInstance();

    // Deze methode haalt de gebruikers uit de database en vult de TableView
    private void loadUsersFromDatabase() {
        if(showDeletedUsers.isSelected())
           users = FXCollections.observableArrayList(userService.getAllUsers());
        else
            users = FXCollections.observableArrayList(userService.getAllActiveUsers());


        filteredUsers = new FilteredList<>(users, p -> true);
        userTable.setItems(filteredUsers);

    }

    @FXML
    public void initialize() {
        loadNavbar();

        loadUsersFromDatabase();
        setupTable();
        setupSearch();

        addUserButton.setText("+");
        addUserButton.setOnAction(e -> addUser());

        showDeletedUsers.selectedProperty().addListener((observable, oldValue, newValue) -> {
            loadUsersFromDatabase();
        });
    }

    // Laad de Navbar FXML en voeg het toe aan de root layout
    private void loadNavbar() {
        try {
            FXMLLoader navbarLoader = new FXMLLoader(getClass().getResource("/view/Navbar.fxml"));
            Parent navbar = navbarLoader.load();

            // Voeg de navbar toe aan de bovenkant van de BorderPane
            rootLayout.getChildren().add(0, navbar);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setupTable() {
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
        HBox hbox = new HBox(5); // 5 is de spacing tussen de knoppen
        hbox.setAlignment(Pos.CENTER);

        MFXButton editButton = new MFXButton("Edit");
        editButton.setOnAction(event -> {
            // Logica voor edit user
            editUser(user);
        });

        MFXButton deleteButton = new MFXButton("Delete");
        deleteButton.setOnAction(event -> {
            // Logica voor delete user
            deleteUser(user);
        });

        hbox.getChildren().addAll(editButton, deleteButton);

        MFXTableRowCell<User, String> cell = new MFXTableRowCell<>(u -> "");
        cell.setGraphic(hbox); // setGraphic is voor alles wanneer je geen tekst aan een cell wil toevoegen
        return cell;
    });

    userTable.getTableColumns().addAll(firstNameColumn, lastNameColumn, emailColumn, addressColumn, roleColumn, statusColumn, actionsColumn);

    userTable.setFooterVisible(false);
    searchField.setPromptText("Search name...");
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
            users.remove(user);
        } else {
            System.out.println("Failed to delete user with email: " + user.getEmail());
        }
    }

    private void editUser(User user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/UserForm.fxml"));
            Parent root = loader.load();
            System.out.println(user);

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
