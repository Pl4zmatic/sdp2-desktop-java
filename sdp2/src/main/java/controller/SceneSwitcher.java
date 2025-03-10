package controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneSwitcher {

    private static Stage stage;
    private static double windowX;
    private static double windowY;
    private static double windowWidth;
    private static double windowHeight;

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

    public static void switchScene(String fxmlPath) throws IOException {
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

            Scene scene = new Scene(root);

            // Zet de nieuwe scène en herstel grootte en positie van het venster
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
}
