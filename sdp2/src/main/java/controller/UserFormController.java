package controller;

import domein.user.UserService;
import domein.user.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import utils.Rollen;
import java.io.IOException;

public class UserFormController {
    @FXML private BorderPane rootLayout;
    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField emailField;
    @FXML private TextField adressField;
    @FXML private VBox passwordContainer;
    @FXML private PasswordField passwordField;
    @FXML private TextField phoneNumberField;
    @FXML private ComboBox<Rollen> roleField;
    @FXML private RadioButton activeButton;
    @FXML private RadioButton inactiveButton;
    @FXML private Button save;
    @FXML private Button resetPasswordButton;

    private final UserService userService = UserService.getInstance();
    private User currentUser;
    private boolean isEditMode;

    @FXML
    public void initialize() {
        rootLayout.setLeft(NavbarManager.getNavbar());
        ObservableList<Rollen> roles = FXCollections.observableArrayList(Rollen.values());
        roleField.setItems(roles);
        ToggleGroup statusGroup = new ToggleGroup();
        activeButton.setToggleGroup(statusGroup);
        inactiveButton.setToggleGroup(statusGroup);
        activeButton.setSelected(true);
        resetPasswordButton.setOnAction(e -> {
            try {
                resetPassword(currentUser, currentUser.getFirstName().toLowerCase());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        resetPasswordButton.getStyleClass().add("red-button");
    }

    @FXML
    private void handleSaveUser() {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String adres = adressField.getText();
        String phoneNumber = phoneNumberField.getText();
        Rollen role = roleField.getValue();
        boolean isActive = activeButton.isSelected();

        boolean success;
        if (isEditMode) {
            currentUser.setFirstName(firstName);
            currentUser.setLastName(lastName);
            currentUser.setEmail(email);
            currentUser.setAdres(adres);
            currentUser.setGsmNummer(phoneNumber);
            currentUser.setRol(role);
            currentUser.setDeleted(!isActive);

            success = userService.editUser(currentUser);
        } else {
            User newUser = new User(firstName, lastName, email, password, adres, phoneNumber, role);
            newUser.setDeleted(!isActive);
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

    public void setUser(User user) {
        this.currentUser = user;
        this.isEditMode = true;
        firstNameField.setText(user.getFirstName());
        lastNameField.setText(user.getLastName());
        emailField.setText(user.getEmail());
        adressField.setText(user.getAdres());
        phoneNumberField.setText(user.getGsmNummer());
        roleField.setValue(user.getRol());
        if (user.getDeleted()) {
            inactiveButton.setSelected(true);
            activeButton.setSelected(false);
        } else {
            activeButton.setSelected(true);
            inactiveButton.setSelected(false);
        }
    }

    public void setEditMode(boolean isEditMode) {
        this.isEditMode = isEditMode;
        if (isEditMode) {
            save.setText("Update");
            passwordContainer.setManaged(false);
            passwordField.setManaged(false);
        } else {
            save.setText("Add");
            passwordContainer.setManaged(true);
        }
    }

    private void resetPassword(User user, String newPw) throws IOException {
        userService.resetPassword(user, newPw);
        showAlert("Succes!", "The password has been succesfully reset.", Alert.AlertType.INFORMATION);
        SceneSwitcher.switchScene("/view/ManageUsers.fxml");
    }
}
