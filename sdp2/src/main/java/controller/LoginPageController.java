package controller;

import domein.Session;
import domein.user.User;

import domein.polling.NotificationPoller;
import service.UserService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import utils.Rollen;

import java.io.IOException;

public class LoginPageController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    private final UserService userService;

    public LoginPageController() {
        this.userService = UserService.getInstance();
    }

    @FXML
    private void initialize() {
        System.out.println(Session.getCurrentUser());
    }

    @FXML
    private void handleLogin() throws IOException {
        String email = emailField.getText();
        String password = passwordField.getText();

        if (userService.login(email, password)) {
            String nextScene = getNextSceneString();

            SceneSwitcher.switchScene(nextScene);

            NavbarController navbarController = (NavbarController) NavbarManager.getNavbar().getUserData();
            if (navbarController != null) {
                navbarController.updateNavbar();
                NotificationPoller.getInstance().addObserver(navbarController);
                NotificationPoller.getInstance().startPolling();
                
            }

        } else {
            showAlert("Login Mislukt", "Ongeldige gebruikersnaam of wachtwoord.", Alert.AlertType.ERROR);
        }
    }

    private static String getNextSceneString() {
        User currentUser = Session.getCurrentUser();

        String nextScene = "";
        if (currentUser.getRol() == Rollen.ADMINISTRATOR) {
            nextScene = "/view/ManageUsers.fxml";
        } else if (currentUser.getRol() == Rollen.VERANTWOORDELIJKE) {
            nextScene = "/view/ManageMachines.fxml";
        } else if (currentUser.getRol() == Rollen.TECHNIEKER) {
            nextScene = "/view/OnderhoudScherm.fxml";
        } else if (currentUser.getRol() == Rollen.MANAGER) {
            nextScene = "/view/ManageMachines.fxml";
        }
        return nextScene;
    }


    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
