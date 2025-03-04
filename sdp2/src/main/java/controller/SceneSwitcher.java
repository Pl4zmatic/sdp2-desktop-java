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

        FXMLLoader loader = new FXMLLoader(SceneSwitcher.class.getResource(fxmlPath));
        Parent root = loader.load();
        stage.setScene(new Scene(root));
        stage.show();
    }
}
