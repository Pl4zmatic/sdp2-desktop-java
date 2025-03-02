package controller;

import domein.user.UserService;
import domein.user.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.scene.layout.HBox;
import utils.Rollen;
import java.io.IOException;

public class UserFormController {

    @FXML private HBox rootLayout;
    @FXML private MFXTextField firstNameField;
    @FXML private MFXTextField lastNameField;
    @FXML private MFXTextField emailField;
    @FXML private MFXTextField passwordField;
    @FXML private MFXTextField adressField;
    @FXML private MFXTextField phoneNumberField;
    @FXML private MFXComboBox<Rollen> roleField;

    private final UserService userService = UserService.getInstance();

    @FXML
    public void initialize() {
        loadNavbar();
        ObservableList<Rollen> roles = FXCollections.observableArrayList(Rollen.values());
        roleField.setItems(roles);
    }

    @FXML
    private void handleSaveUser() {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String adres = adressField.getText();
        String phoneNumber = phoneNumberField.getText();
        Rollen role = roleField.getSelectedItem();

        boolean success = userService.register(firstName, lastName, email, password, adres, phoneNumber, role);

        if (success) {
            showAlert("Success", "User successfully created!", Alert.AlertType.INFORMATION);
            try {
                SceneSwitcher.switchScene("/view/ManageUsers.fxml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            showAlert("Error", "Failed to create user. Email might already exist.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleCancel() {
        try {
            SceneSwitcher.switchScene("/view/ManageUsers.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void loadNavbar() {
        try {
            FXMLLoader navbarLoader = new FXMLLoader(getClass().getResource("/view/Navbar.fxml"));
            Parent navbar = navbarLoader.load();
            rootLayout.getChildren().add(0, navbar);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
