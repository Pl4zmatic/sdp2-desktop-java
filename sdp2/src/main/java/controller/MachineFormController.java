package controller;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import lombok.Setter;

public class MachineFormController {

    @Setter
    private HBox parent;

    @FXML
    private HBox rootLayout;

    // text fields
    @FXML
    private TextField machineCode;
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
        setupCallbacks();
    }

    private void setupCallbacks() {
        cancel.setOnAction((event) -> {
            try {
                cancelCallback();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        machineCode.addEventHandler(MouseEvent.MOUSE_CLICKED, (event) -> editMachineCode());
    }

    private void cancelCallback() throws IOException {
        this.parent.getChildren().remove(this.rootLayout);
    }

    private void editMachineCode() {
        machineCode.setStyle("-fx-background-color: white;");
        machineCode.clear();
    }
}
