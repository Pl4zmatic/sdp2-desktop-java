package controller;

import domein.user.LoginService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;

import java.io.IOException;

public class LoginPageController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    private final LoginService loginSerivce;

    public LoginPageController() {
        this.loginSerivce = new LoginService();
    }

    @FXML
    private void handleLogin() {
        String email = emailField.getText();
        String password = passwordField.getText();
        System.out.println("DIT: " + password);



        if (loginSerivce.login(email, password)) {
            try{
                SceneSwitcher.switchScene("/view/LandingPage.fxml");
            } catch(IOException e) {
                e.printStackTrace();
            }

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
