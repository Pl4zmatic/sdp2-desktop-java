package controller;

import domein.Session;
import domein.user.UserService;
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

    private final UserService userSerivce;

    public LoginPageController() {
        this.userSerivce = UserService.getInstance();
    }

    @FXML
    private void initialize() {
        System.out.println(Session.getCurrentUser());
    }

    @FXML
    private void handleLogin() {
        String email = emailField.getText();
        String password = passwordField.getText();

        if (userSerivce.login(email, password)) {
            NavbarController navbarController = (NavbarController) NavbarManager.getNavbar().getUserData();
            if (navbarController != null) {
                navbarController.updateNavbar();
            }

            try{
                SceneSwitcher.switchScene("/view/ManageUsers.fxml");
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
