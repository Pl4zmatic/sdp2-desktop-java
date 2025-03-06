package controller;

import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;

public class UserLogsController {
    @FXML
    private BorderPane rootLayout;

    @FXML
    public void initialize() {
        rootLayout.setLeft(NavbarManager.getNavbar());
    }
}
