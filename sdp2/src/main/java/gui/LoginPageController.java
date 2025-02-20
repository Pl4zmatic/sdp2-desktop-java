package gui;

import domein.user.LoginService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;

public class LoginPageController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    private final LoginService loginController;

    public LoginPageController() {
        this.loginController = new LoginService();
    }

    @FXML
    private void handleLogin() {
        String email = emailField.getText();
        String password = passwordField.getText();

        if (loginController.login(email, password)) {
            showAlert("Login Succesvol!", "Welkom, " + email + "!", Alert.AlertType.INFORMATION);
        } else {
            showAlert("Login Mislukt", "Ongeldige gebruikersnaam of wachtwoord.", Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
