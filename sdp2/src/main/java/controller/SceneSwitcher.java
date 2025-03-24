package controller;

import domein.Session;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneSwitcher {

    private static Stage stage;
    private static double windowX;
    private static double windowY;
    private static double windowWidth;
    private static double windowHeight;

    private static Object currentController;

    public static Stage getStage() {
        if (stage == null) {
            throw new IllegalStateException("Stage is niet ingesteld. Roep eerst setStage() aan.");
        }
        return stage;
    }

    public static void setStage(Stage mainStage) {
        stage = mainStage;
        // Initialiseer de beginpositie en grootte
        windowX = stage.getX();
        windowY = stage.getY();
        windowWidth = stage.getWidth();
        windowHeight = stage.getHeight();
    }

    public static void switchScene(String fxmlPath) {
        NavbarManager.reloadNavbar();
        if (stage == null) {
            throw new IllegalStateException("Stage is niet ingesteld. Roep eerst setStage() aan.");
        }

        try {
            // Sla huidige grootte en positie van het venster op
            windowX = stage.getX();
            windowY = stage.getY();
            windowWidth = stage.getWidth();
            windowHeight = stage.getHeight();

            FXMLLoader loader = new FXMLLoader(SceneSwitcher.class.getResource(fxmlPath));
            Parent root = loader.load();

            currentController = loader.getController();

            if (root instanceof BorderPane) {
                NavbarManager.reloadNavbar();
            }

            Scene scene = new Scene(root);

            stage.setScene(scene);
            stage.setX(windowX);
            stage.setY(windowY);
            stage.setWidth(windowWidth);
            stage.setHeight(windowHeight);
            stage.show();

            System.out.println("Scène succesvol geladen: " + fxmlPath);
        } catch (IOException e) {
            System.err.println("Kan FXML-bestand niet laden: " + fxmlPath);
            e.printStackTrace();
        }
    }

    public static <T> T getCurrentController() {
        return (T) currentController;
    }
}

