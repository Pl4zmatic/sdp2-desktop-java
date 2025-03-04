package controller;

import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.Map;

public class MenuController {

    @FXML
    private VBox navbarRoot;

    @FXML private Button btnMenu1;
    @FXML private Button btnMenu2;
    @FXML private Button btnMenu3;

    @FXML private VBox subMenu1;
    @FXML private VBox subMenu2;
    @FXML private VBox subMenu3;

    @FXML private Text welcomeName;
    @FXML private Text profileName;

    @FXML private Button manageMachines;

    private Map<Button, VBox> menuMap = new HashMap<>();
    private Map<VBox, Boolean> visibilityMap = new HashMap<>();

    @FXML
    private void initialize() {
        // Verberg submenu's bij opstarten
        setMenuVisibility(subMenu1, false);
        setMenuVisibility(subMenu2, false);
        setMenuVisibility(subMenu3, false);

        menuMap.put(btnMenu1, subMenu1);
        menuMap.put(btnMenu2, subMenu2);
        menuMap.put(btnMenu3, subMenu3);

        // Knoppen koppelen aan toggle functie
        menuMap.forEach((button, submenu) -> {
            button.setOnAction(event -> toggleSubMenu(submenu, button));
        });

        // Zet de tekst naar de gebruikersnaam
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

        // Update zichtbaarheid
        visibilityMap.put(submenu, true);

        // Pas pijltje aan
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

        // Update zichtbaarheid
        visibilityMap.put(submenu, false);

        // Pas pijltje aan
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
