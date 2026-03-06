package main;
import controller.SceneSwitcher;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        SceneSwitcher.setStage(stage);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/LoginPage.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Shopfloor");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        // new populateDb().main(null);
        launch();
    }
}
