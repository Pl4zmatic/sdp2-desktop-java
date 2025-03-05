package controller;

import controller.SceneSwitcher;
import domein.Session;
import domein.user.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import utils.Rollen;

import java.io.IOException;

public class NavbarController {

    @FXML private VBox administratorMenu;
    @FXML private VBox verantwoordelijkeMenu;
    @FXML private VBox techniekerMenu;

    @FXML private Button beheerGebruikerItem;
    @FXML private Button logItem;
    @FXML private Button beheerSiteItem;
    @FXML private Button onderhoudItem;
    @FXML private Button beheerMachineItem;
    @FXML private Button beheerNotificatieItem;
    @FXML private Button onderhoudTechniekerItem;

    @FXML private Text profileName;

    private Button activeButton;

    @FXML
    private void initialize() {
        User curUser = Session.getCurrentUser();
        Rollen userRole = curUser.getRol();
        Button activeNavItem = Session.getActiveNavItem();

        switch (userRole) {
            case ADMINISTRATOR -> {
                administratorMenu.setManaged(true);
                verantwoordelijkeMenu.setManaged(false);
                verantwoordelijkeMenu.setVisible(false);
                techniekerMenu.setManaged(false);
                techniekerMenu.setVisible(false);
            }
            case VERANTWOORDELIJKE -> {
                administratorMenu.setManaged(false);
                administratorMenu.setVisible(false);
                verantwoordelijkeMenu.setManaged(true);
                techniekerMenu.setManaged(false);
                techniekerMenu.setVisible(false);
            }
            case TECHNIEKER -> {
                administratorMenu.setManaged(false);
                administratorMenu.setVisible(false);
                verantwoordelijkeMenu.setManaged(false);
                verantwoordelijkeMenu.setVisible(false);
                techniekerMenu.setManaged(true);
            }
        }

        // Stel de navigatieknoppen in
        for (Button button : getAllMenuButtons()) {
            button.setOnAction(event -> {
                try {
                    handleNavigation(button);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        setTextToUsername(profileName, curUser.getFirstName() + " " + curUser.getLastName());
    }

    private void handleNavigation(Button clickedButton) throws IOException {
        if (Session.getActiveNavItem() != null) {
            Session.getActiveNavItem().getStyleClass().remove("active");
        }

        clickedButton.getStyleClass().add("active");

        Session.setActiveButton(clickedButton);


        String fxmlPath = switch (clickedButton.getText()) {
            case "Beheer Gebruikers" -> "/view/ManageUsers.fxml";
            case "Logs" -> "/view/Logs.fxml";
            case "Beheer Sites" -> "/view/SitesManagement.fxml";
            case "Onderhoud" -> "/view/Maintenance.fxml";
            case "Beheer Machines" -> "/view/ManageMachines.fxml";
            case "Beheer Notificaties" -> "/view/NotificationsManagement.fxml";
            case "Logout" -> "/view/Logout.fxml";
            default -> null;
        };

        if (fxmlPath != null) {
            SceneSwitcher.switchScene(fxmlPath);
        }
    }


    private Button[] getAllMenuButtons() {
        return new Button[]{
                beheerGebruikerItem,
                logItem,
                beheerSiteItem,
                onderhoudItem,
                beheerMachineItem,
                beheerNotificatieItem,
                onderhoudTechniekerItem,
        };
    }

    private void setTextToUsername(Text text, String fullName) {
        text.setText(fullName);
    }
}
