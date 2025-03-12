package controller;

import domein.Session;
import domein.user.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
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

    @FXML private Text profileLastName;
    @FXML private Text profileFirstName;
    @FXML private Button profileIcon;
    @FXML private ContextMenu logoutMenu;
    @FXML private MenuItem logoutItem;

    @FXML
    public void initialize() {
        updateNavbar();

        for (Button button : getAllMenuButtons()) {
            button.setOnAction(event -> {
                try {
                    handleNavigation(button);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    public void updateNavbar() {
        User currentUser = Session.getCurrentUser();

        Rollen userRole = currentUser.getRol();
        switch (userRole) {
            case ADMINISTRATOR -> {
                administratorMenu.setManaged(true);
                administratorMenu.setVisible(true);
                verantwoordelijkeMenu.setManaged(false);
                verantwoordelijkeMenu.setVisible(false);
                techniekerMenu.setManaged(false);
                techniekerMenu.setVisible(false);
            }
            case VERANTWOORDELIJKE -> {
                administratorMenu.setManaged(false);
                administratorMenu.setVisible(false);
                verantwoordelijkeMenu.setManaged(true);
                verantwoordelijkeMenu.setVisible(true);
                techniekerMenu.setManaged(false);
                techniekerMenu.setVisible(false);
            }
            case TECHNIEKER -> {
                administratorMenu.setManaged(false);
                administratorMenu.setVisible(false);
                verantwoordelijkeMenu.setManaged(false);
                verantwoordelijkeMenu.setVisible(false);
                techniekerMenu.setManaged(true);
                techniekerMenu.setVisible(true);
            }
        }

        setTextToUsername(profileLastName, currentUser.getLastName());
        setTextToUsername(profileFirstName, currentUser.getFirstName());

        for (Button button : getAllMenuButtons()) {
            button.setText(button.getText().replace("➡ ", ""));
        }

        setupProfileMenu();
    }

    private void handleNavigation(Button clickedButton) throws IOException {
        for (Button button : getAllMenuButtons()) {
            button.setText(button.getText().replace("➡ ", ""));
        }

        clickedButton.setText("➡ " + clickedButton.getText());

        Session.setActiveButton(clickedButton);


        String fxmlPath = switch (clickedButton.getText().replace("➡ ", "")) {
            case "Beheer Gebruikers" -> "/view/ManageUsers.fxml";
            case "Logs" -> "/view/UserLogs.fxml";
            case "Beheer Sites" -> "/view/SitesManagement.fxml";
            case "Onderhoud" -> "/view/OnderhoudScherm.fxml";
            case "Beheer Machines" -> "/view/ManageMachines.fxml";
            case "Beheer Notificaties" -> "/view/NotificationsManagement.fxml";
            case "Logout" -> "/view/Logout.fxml";
            default -> null;
        };

        if (fxmlPath != null) {
            SceneSwitcher.switchScene(fxmlPath);
        }
        System.out.println(fxmlPath);
    System.out.println(clickedButton.getText());
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

    private void setupProfileMenu() {
        setTextToUsername(profileLastName, Session.getCurrentUser().getLastName());
        setTextToUsername(profileFirstName, Session.getCurrentUser().getFirstName());

        logoutMenu.getStyleClass().add("logoutMenu");
        logoutItem.getStyleClass().add("logoutItem");

        profileIcon.setOnMouseClicked(e -> {
            if(e.getButton().equals(MouseButton.PRIMARY)) {
                logoutMenu.show(profileIcon, e.getScreenX(), e.getScreenY());
            }
        });

        logoutItem.setOnAction(e -> {
            try {
                handleLougout();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    private void handleLougout() throws IOException {
        Session.clear();
        SceneSwitcher.switchScene("/view/LoginPage.fxml");
    }

    private void setTextToUsername(Text text, String fullName) {
        text.setText(fullName);
    }
}
