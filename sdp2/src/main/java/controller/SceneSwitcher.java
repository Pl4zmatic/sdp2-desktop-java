package controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneSwitcher {

    private static Stage stage;

    public static Stage getStage() {
        if (stage == null) {
            throw new IllegalStateException("Stage is niet ingesteld. Roep eerst setStage() aan.");
        }
        return stage;
    }

    public static void setStage(Stage mainStage) {
        stage = mainStage;
    }

    public static void switchScene(String fxmlPath) throws IOException {
        if (stage == null) {
            throw new IllegalStateException("Stage is niet ingesteld. Roep eerst setStage() aan.");
        }

        try {
            FXMLLoader loader = new FXMLLoader(SceneSwitcher.class.getResource(fxmlPath));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

            System.out.println("Scène succesvol geladen: " + fxmlPath);
        } catch (IOException e) {
            System.err.println("Kan FXML-bestand niet laden: " + fxmlPath);
            e.printStackTrace();
        }
    }
}
