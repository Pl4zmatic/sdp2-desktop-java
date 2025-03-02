package controller;

import domein.user.User;
import domein.user.UserService;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import java.io.IOException;
import java.util.Comparator;

public class GebruikersBeheerderController {

    @FXML private HBox rootLayout;
    @FXML private MFXTableView<User> userTable;
    @FXML private MFXTextField searchField;
    @FXML private MFXButton addUserButton;

    private ObservableList<User> users = FXCollections.observableArrayList();
    private FilteredList<User> filteredUsers;
    private UserService userService = UserService.getInstance();

    // Deze methode haalt de gebruikers uit de database en vult de TableView
    private void loadUsersFromDatabase() {
        users.setAll(userService.getAllUsers());
        filteredUsers = new FilteredList<>(users, p -> true);
        userTable.setItems(filteredUsers);  // De TableView wordt gevuld met de lijst van gebruikers
    }

    @FXML
    public void initialize() {
        loadNavbar();

        loadUsersFromDatabase();
        setupTable();
        setupSearch();
        addUserButton.setText("+");
        addUserButton.setOnAction(e -> addUser());
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

    // Maak de kolommen van de MFXTableView
    private void setupTable() {
        MFXTableColumn<User> firstNameColumn = new MFXTableColumn<>("First Name", true, Comparator.comparing(User::getFirstName));
        MFXTableColumn<User> lastNameColumn = new MFXTableColumn<>("Last Name", true, Comparator.comparing(User::getLastName));
        MFXTableColumn<User> emailColumn = new MFXTableColumn<>("Email", true, Comparator.comparing(User::getEmail));
        MFXTableColumn<User> addressColumn = new MFXTableColumn<>("Address", true, Comparator.comparing(User::getAdres));
        MFXTableColumn<User> roleColumn = new MFXTableColumn<>("Role", true, Comparator.comparing(User::getRol));

        // Stel voor elke kolom in welke gegevens de cellen moeten bevatten
        firstNameColumn.setRowCellFactory(user -> new MFXTableRowCell<>(User::getFirstName));
        lastNameColumn.setRowCellFactory(user -> new MFXTableRowCell<>(User::getLastName));
        emailColumn.setRowCellFactory(user -> new MFXTableRowCell<>(User::getEmail));
        addressColumn.setRowCellFactory(user -> new MFXTableRowCell<>(User::getAdres));
        roleColumn.setRowCellFactory(user -> new MFXTableRowCell<>(User::getRol));

        // Voeg de kolommen toe aan de MFXTableView
        userTable.getTableColumns().addAll(firstNameColumn, lastNameColumn, emailColumn, addressColumn, roleColumn);

        //disable filter sectie
        userTable.setFooterVisible(false);

        //set prompt text voor zoekbalk
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

//    private void EditUser(){
//
//    }

}
