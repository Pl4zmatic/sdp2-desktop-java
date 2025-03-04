package controller;

import domein.user.UserService;
import domein.user.User;
import io.github.palexdev.materialfx.controls.MFXCheckbox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.HBox;
import lombok.Setter;
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
    @FXML private MFXButton saveButton;
    @FXML private CheckBox isDeletedButton;

    private final UserService userService = UserService.getInstance();
    private User currentUser;
    private boolean isEditMode;

    @FXML
    public void initialize() {
        System.out.println(isEditMode);
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
    boolean isDeleted = !isDeletedButton.isSelected();

    User newUser = new User(firstName, lastName, email, password, adres, phoneNumber, role);
    newUser.setDeleted(isDeleted);
    System.out.println(newUser);

    boolean success;
    if (isEditMode) {
        // Bewerk de bestaande gebruiker
        success = userService.editUser(newUser);
    } else {
        // Voeg een nieuwe gebruiker toe
        success = userService.register(firstName, lastName, email, password, adres, phoneNumber, role);
    }

    if (success) {
        String message = isEditMode ? "User successfully updated!" : "User successfully created!";
        showAlert("Success", message, Alert.AlertType.INFORMATION);
        try {
            SceneSwitcher.switchScene("/view/ManageUsers.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    } else {
        showAlert("Error", "Failed to save user. Email might already exist.", Alert.AlertType.ERROR);
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

    public void setUser(User user) {
        this.currentUser = user;
        this.isEditMode = true;
        firstNameField.setText(user.getFirstName());
        lastNameField.setText(user.getLastName());
        emailField.setText(user.getEmail());
        passwordField.setText(user.getPassword());
        adressField.setText(user.getAdres());
        phoneNumberField.setText(user.getGsmNummer());
        roleField.getSelectionModel().selectItem(user.getRol());;
        isDeletedButton.setSelected(!user.getDeleted());
    }

public void setEditMode(boolean isEditMode)
    {
        this.isEditMode = isEditMode;
        if (isEditMode) {
            saveButton.setText("Update");
        } else saveButton.setText("Add");
    }
}
