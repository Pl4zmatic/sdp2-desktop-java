package controller;

import service.UserService;
import domein.user.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import utils.Rollen;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UserFormController {
    @FXML private BorderPane rootLayout;
    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private DatePicker birthDatePicker;
    @FXML private TextField emailField;
    @FXML private TextField adressField;
    @FXML private PasswordField passwordField;
    @FXML private TextField phoneNumberField;
    @FXML private ComboBox<Rollen> roleField;
    @FXML private RadioButton activeButton;
    @FXML private RadioButton inactiveButton;
    @FXML private Button save;
    @FXML private Button resetPasswordButton;
    @FXML private HBox passwordResetContainer;
    @FXML private VBox passwordContainer;
    @FXML private Label phoneRequiredLabel;
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

        // Add listener to role field to update validation requirements
        roleField.valueProperty().addListener((obs, oldVal, newVal) -> {
            // Update visual cues for phone number field based on role
            updatePhoneNumberRequirement();
        });
    }

    private void updatePhoneNumberRequirement() {
        // If role is Technieker, add visual indication that phone number is required
        if (roleField.getValue() == Rollen.TECHNIEKER) {
            if (!phoneNumberField.getStyleClass().contains("required-field")) {
                phoneNumberField.getStyleClass().add("required-field");
            }
        } else {
            phoneNumberField.getStyleClass().removeAll("required-field");
        }
    }

    @FXML
    private void handleSaveUser() {
        if (!validateForm()) {
            return; // Stop if validation fails
        }

        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        LocalDate birthDate = birthDatePicker.getValue();
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
            currentUser.setBirthDate(birthDate);
            currentUser.setEmail(email);
            currentUser.setAdres(adres);
            currentUser.setGsmNummer(phoneNumber);
            currentUser.setRol(role);
            currentUser.setDeleted(!isActive);

            success = userService.editUser(currentUser);
        } else {
            User newUser = new User(firstName, lastName, birthDate,email, password, adres, phoneNumber, role);
            newUser.setDeleted(!isActive);
            success = userService.register(firstName, lastName, birthDate,email, password, adres, phoneNumber, role);
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

    private boolean validateForm() {
        List<String> errors = new ArrayList<>();

        // Check mandatory fields
        if (firstNameField.getText().trim().isEmpty()) {
            errors.add("First name is required");
            firstNameField.getStyleClass().add("error-field");
        } else {
            firstNameField.getStyleClass().removeAll("error-field");
        }

        if (lastNameField.getText().trim().isEmpty()) {
            errors.add("Last name is required");
            lastNameField.getStyleClass().add("error-field");
        } else {
            lastNameField.getStyleClass().removeAll("error-field");
        }

        if (birthDatePicker.getValue() == null) {
            errors.add("Birth date is required");
            birthDatePicker.getStyleClass().add("error-field");
        } else {
            if (birthDatePicker.getValue().isAfter(LocalDate.now())) {
                errors.add("Birth date cannot be in the future");
                birthDatePicker.getStyleClass().add("error-field");
            } else {
                birthDatePicker.getStyleClass().removeAll("error-field");
            }
        }

        if (emailField.getText().trim().isEmpty()) {
            errors.add("Email is required");
            emailField.getStyleClass().add("error-field");
        } else {
            if (!emailField.getText().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                errors.add("Email format is invalid");
                emailField.getStyleClass().add("error-field");
            } else {
                emailField.getStyleClass().removeAll("error-field");
            }
        }

        if (adressField.getText().trim().isEmpty()) {
            errors.add("Address is required");
            adressField.getStyleClass().add("error-field");
        } else {
            adressField.getStyleClass().removeAll("error-field");
        }

        if (!isEditMode && passwordField.getText().trim().isEmpty()) {
            errors.add("Password is required");
            passwordField.getStyleClass().add("error-field");
        } else {
            passwordField.getStyleClass().removeAll("error-field");
        }

        if (roleField.getValue() == null) {
            errors.add("Role is required");
            roleField.getStyleClass().add("error-field");
        } else {
            roleField.getStyleClass().removeAll("error-field");

            if (roleField.getValue() == Rollen.TECHNIEKER && phoneNumberField.getText().trim().isEmpty()) {
                errors.add("Phone number is required for Technieker role");
                phoneNumberField.getStyleClass().add("error-field");
            } else {
                phoneNumberField.getStyleClass().removeAll("error-field");
            }
        }

        if (!errors.isEmpty()) {
            showValidationErrors(errors);
            return false;
        }

        return true;
    }

    private void showValidationErrors(List<String> errors) {
        StringBuilder errorMessage = new StringBuilder("Please correct the following errors:\n\n");
        for (String error : errors) {
            errorMessage.append("• ").append(error).append("\n");
        }

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Validation Error");
        alert.setHeaderText("Form contains errors");
        alert.setContentText(errorMessage.toString());
        alert.showAndWait();
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
        birthDatePicker.setValue(user.getBirthDate());
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

        updatePhoneNumberRequirement();

        if (user.getRol() == Rollen.TECHNIEKER) {
            phoneRequiredLabel.setVisible(true);
        } else {
            phoneRequiredLabel.setVisible(false);
        }
    }

    public void setEditMode(boolean isEditMode) {
        this.isEditMode = isEditMode;
        if (isEditMode) {
            save.setText("Update");
            passwordResetContainer.setManaged(true);
            passwordResetContainer.setVisible(true);
            resetPasswordButton.setVisible(true);
            resetPasswordButton.setVisible(true);
            passwordContainer.setManaged(false);
            passwordContainer.setVisible(false);

        } else {
            save.setText("Add");
            passwordResetContainer.setManaged(false);
            passwordResetContainer.setManaged(false);
            resetPasswordButton.setVisible(false);
            resetPasswordButton.setVisible(false);
            passwordContainer.setManaged(true);
            passwordContainer.setVisible(true);
        }
    }

    private void resetPassword(User user, String newPw) throws IOException {
        userService.resetPassword(user, newPw);
        showAlert("Succes!", "The password has been succesfully reset.", Alert.AlertType.INFORMATION);
        SceneSwitcher.switchScene("/view/ManageUsers.fxml");
    }
}

