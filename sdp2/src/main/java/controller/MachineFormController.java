package controller;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class MachineFormController {
    @FXML
    private HBox rootLayout;

    // text fields
    @FXML
    private Label machineCode;
    @FXML
    private TextField site;
    @FXML
    private TextField machineLoc;
    @FXML
    private TextField technician;
    @FXML
    private DatePicker lastMaintenance;
    @FXML
    private Label details;
    @FXML
    private DatePicker nextMaintenance;
    @FXML
    private TextArea productInfo;

    // uptime
    @FXML
    private TextField days;
    @FXML
    private TextField hours;
    @FXML
    private TextField minutes;

    // status
    @FXML
    private RadioButton active;
    @FXML
    private RadioButton inactive;

    // production status
    @FXML
    private RadioButton failing;
    @FXML
    private RadioButton maintenance;
    @FXML
    private RadioButton healthy;

    // buttons
    @FXML
    private Button save;
    @FXML
    private Button cancel;

    @FXML
    private void initialize() {
        try {
            setupNavbar();
            setupCallbacks();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setupCallbacks() {
        cancel.setOnAction((event) -> {
            try {
                cancelCallback();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void cancelCallback() throws IOException {
        SceneSwitcher.switchScene("/view/ManageMachines.fxml");
    }

    private void setupNavbar() throws IOException {
        Node sideBar = new FXMLLoader(getClass().getResource("/view/Navbar.fxml")).load();
        rootLayout.getChildren().add(0, sideBar);
    }
}
