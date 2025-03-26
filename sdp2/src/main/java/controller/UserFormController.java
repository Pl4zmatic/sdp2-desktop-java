package controller;

import service.ServiceController;
import service.UserService;
import domein.user.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private DatePicker birthDatePicker;
    @FXML private TextField emailField;
    @FXML private TextField streetField;
    @FXML private TextField houseNumberField;
    @FXML private TextField postalCodeField;
    @FXML private TextField cityField;
    @FXML private PasswordField passwordField;
    @FXML private TextField phoneNumberField;
    @FXML private ComboBox<Rollen> roleField;
    @FXML private RadioButton activeButton;
    @FXML private RadioButton inactiveButton;
    @FXML private Button save;
    @FXML private Button cancel;
    @FXML private Button resetPasswordButton;
    @FXML private HBox passwordResetContainer;
    @FXML private VBox passwordContainer;
    @FXML private Label phoneRequiredLabel;

    private final ServiceController sc = ServiceController.getInstance();
    private User currentUser;
    private boolean isEditMode;

    private Runnable onSaveCallback;
    private EventHandler<ActionEvent> closeHandler;

    @FXML
    public void initialize() {

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

        roleField.valueProperty().addListener((obs, oldVal, newVal) -> {
            updatePhoneNumberRequirement();
        });
    }

    private void updatePhoneNumberRequirement() {
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
            return;
        }

        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        LocalDate birthDate = birthDatePicker.getValue();
        String email = emailField.getText();
        String password = passwordField.getText();

        String adres = streetField.getText() + " " + houseNumberField.getText() + ", " +
                postalCodeField.getText() + " " + cityField.getText();

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

            success = sc.editUser(currentUser);
        } else {
            User newUser = new User(firstName, lastName, birthDate, email, password, adres, phoneNumber, role);
            newUser.setDeleted(!isActive);
            success = sc.userRegister(firstName, lastName, birthDate, email, password, adres, phoneNumber, role);
        }

        if (success) {
            String message = isEditMode ? "User successfully updated!" : "User successfully created!";
            showAlert("Success", message, Alert.AlertType.INFORMATION);

            if (onSaveCallback != null) {
                onSaveCallback.run();
            }
        } else {
            showAlert("Error", "Failed to save user. Email might already exist.", Alert.AlertType.ERROR);
        }
    }

    private boolean validateForm() {
        List<String> errors = new ArrayList<>();

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

        if (streetField.getText().trim().isEmpty()) {
            errors.add("Street is required");
            streetField.getStyleClass().add("error-field");
        } else {
            streetField.getStyleClass().removeAll("error-field");
        }

        if (houseNumberField.getText().trim().isEmpty()) {
            errors.add("House number is required");
            houseNumberField.getStyleClass().add("error-field");
        } else {
            houseNumberField.getStyleClass().removeAll("error-field");
        }

        if (postalCodeField.getText().trim().isEmpty()) {
            errors.add("Postal code is required");
            postalCodeField.getStyleClass().add("error-field");
        } else {
            postalCodeField.getStyleClass().removeAll("error-field");
        }

        if (cityField.getText().trim().isEmpty()) {
            errors.add("City is required");
            cityField.getStyleClass().add("error-field");
        } else {
            cityField.getStyleClass().removeAll("error-field");
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
        if (closeHandler != null) {
            closeHandler.handle(new ActionEvent());
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

        String address = user.getAdres();
        if (address != null && !address.isEmpty()) {
            String[] parts = address.split(",");
            String streetPart = parts[0].trim();
            String cityPart = parts.length > 1 ? parts[1].trim() : "";

            int lastSpaceIndex = streetPart.lastIndexOf(" ");
            if (lastSpaceIndex > 0) {
                streetField.setText(streetPart.substring(0, lastSpaceIndex));
                houseNumberField.setText(streetPart.substring(lastSpaceIndex + 1));
            } else {
                streetField.setText(streetPart);
                houseNumberField.setText("");
            }

            String[] cityParts = cityPart.split(" ", 2);
            postalCodeField.setText(cityParts.length > 0 ? cityParts[0] : "");
            cityField.setText(cityParts.length > 1 ? cityParts[1] : "");
        }

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
            passwordContainer.setManaged(false);
            passwordContainer.setVisible(false);

        } else {
            save.setText("Add");
            passwordResetContainer.setManaged(false);
            passwordResetContainer.setVisible(false);
            resetPasswordButton.setVisible(false);
            passwordContainer.setManaged(true);
            passwordContainer.setVisible(true);
        }
    }

    private void resetPassword(User user, String newPw) throws IOException {
        sc.resetPassword(user, newPw);
        showAlert("Success!", "The password has been successfully reset.", Alert.AlertType.INFORMATION);

        if (onSaveCallback != null) {
            onSaveCallback.run();
        }
    }

    public void addCloseButton(EventHandler<ActionEvent> closeHandler) {
        this.closeHandler = closeHandler;

        if (cancel != null) {
            cancel.setOnAction(event -> {
                if (this.closeHandler != null) {
                    this.closeHandler.handle(event);
                }
            });
        }
    }

    public void clearForm() {
        firstNameField.clear();
        lastNameField.clear();
        birthDatePicker.setValue(null);
        emailField.clear();
        // Clear address fields
        streetField.clear();
        houseNumberField.clear();
        postalCodeField.clear();
        cityField.clear();
        passwordField.clear();
        phoneNumberField.clear();
        roleField.getSelectionModel().clearSelection();
        activeButton.setSelected(true);
    }

    public void setOnSaveCallback(Runnable callback) {
        this.onSaveCallback = callback;
    }
}

