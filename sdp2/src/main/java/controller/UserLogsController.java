package controller;

import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;

public class LogsController {
    private BorderPane rootLayout;

    @FXML
    public void initialize() {
        rootLayout.setLeft(manager.NavbarManager.getNavbar());
    }
}
