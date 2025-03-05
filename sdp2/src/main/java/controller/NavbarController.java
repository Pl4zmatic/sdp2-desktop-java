package controller;

import domein.Session;
import domein.user.User;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;
import utils.Rollen;

import java.util.HashMap;
import java.util.Map;

public class NavbarController {

    @FXML VBox administratorMenu;
    @FXML VBox verantwoordelijkeMenu;
    @FXML VBox techniekerMenu;

    @FXML Button beheerGebruikerItem;

    @FXML private Text profileName;

    private Button activeButton;
    private Map<Button, VBox> menuMap = new HashMap<>();
    private Map<VBox, Boolean> visibilityMap = new HashMap<>();

    @FXML
    private void initialize() {
        User curUser = Session.getCurrentUser();

        Rollen userRole = curUser.getRol();

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


        // Knoppen koppelen aan toggle functie
        menuMap.forEach((button, submenu) -> {
            button.setOnAction(event -> toggleSubMenu(submenu, button));
        });

        setTextToUsername(profileName, "John Doe");

        //set button manageMachines callback
        manageMachines.setOnAction(event -> switchToManageMachines());
    }

    private void toggleSubMenu(VBox submenu, Button menuButton) {
        boolean isVisible = visibilityMap.getOrDefault(submenu, false);

        if (isVisible) {
            hideSubMenu(submenu, menuButton);
        } else {
            showSubMenu(submenu, menuButton);
        }
    }

    private void showSubMenu(VBox submenu, Button menuButton) {
        submenu.setVisible(true);
        submenu.setManaged(true);

        TranslateTransition transition = new TranslateTransition(Duration.millis(300), submenu);
        transition.setFromY(-10);
        transition.setToY(0);
        transition.play();

        visibilityMap.put(submenu, true);

        menuButton.setText(menuButton.getText().replace("▸", "▾"));
    }

    private void hideSubMenu(VBox submenu, Button menuButton) {
        TranslateTransition transition = new TranslateTransition(Duration.millis(300), submenu);
        transition.setToY(-10);
        transition.setOnFinished(e -> {
            submenu.setVisible(false);
            submenu.setManaged(false);
        });
        transition.play();

        visibilityMap.put(submenu, false);

        menuButton.setText(menuButton.getText().replace("▾", "▸"));
    }

    private void setMenuVisibility(VBox submenu, boolean visible) {
        submenu.setVisible(visible);
        submenu.setManaged(visible);
        visibilityMap.put(submenu, visible);
    }

    private void setTextToUsername(Text text, String fullName) {
        text.setText(fullName);
    }

    private void switchToManageMachines() {
        try {
            SceneSwitcher.switchScene("/view/ManageMachines.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
