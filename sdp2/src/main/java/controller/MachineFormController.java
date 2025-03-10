package controller;

import java.io.IOException;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import domein.machine.Machine;
import domein.machine.RunningState;
import domein.machine.StoppedState;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import lombok.Getter;
import lombok.Setter;

public class MachineFormController {

    @Getter
    @Setter
    private Machine machine;

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
    private VBox lastMaintenanceContainer;
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
    @FXML
    private ToggleGroup status;

    // production status
    @FXML
    private RadioButton failing;
    @FXML
    private RadioButton maintenance;
    @FXML
    private RadioButton healthy;
    @FXML
    private ToggleGroup productionStatus;

    // buttons
    @FXML
    private Button save;
    @FXML
    private Button cancel;

    //error checking
    private List<String> errors = new ArrayList<>();

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

        machineCode.addEventHandler(MouseEvent.MOUSE_CLICKED, (event) -> editMachineCodeCallback());
        machineCode.focusedProperty().addListener((event) -> checkTextField(machineCode, "Code is vereist."));

        site.focusedProperty().addListener((event) -> checkTextField(site, "Site is vereist."));
        machineLoc.focusedProperty().addListener((event) -> checkTextField(machineLoc, "Locatie is vereist."));
        productInfo.focusedProperty().addListener((event) -> checkTextField(productInfo, "Product info is vereist."));

        technician.textProperty().addListener((event) -> checkTechnician());

        lastMaintenance.setOnAction((event) -> checkDate("Datum is vereist."));

        save.setOnAction((event) -> saveMachine());
    }

    private void cancelCallback() throws IOException {
        this.parent.getChildren().remove(this.rootLayout);
    }

    private void editMachineCodeCallback() {
        machineCode.setStyle("-fx-background-color: white;");
        machineCode.setText("");
    }

    private <T extends TextInputControl> void checkTextField(T source, String error) {
        errors.remove(error);
        if ((source.getText().isBlank() || source.getText().isEmpty()) && !source.isFocused()) {
            source.setPromptText(error);
            source.setStyle("-fx-prompt-text-fill: -bgRed;");
            errors.add(error);
        }
    }

    private void checkTechnician() {
        if (!technician.getText().isBlank() && !technician.getText().isEmpty()) {
            lastMaintenanceContainer.setDisable(false);
            if (this.machine == null) {
                details.setDisable(true);
            }
        } else {
            lastMaintenanceContainer.setDisable(true);
        }
    }

    private void checkDate(String error) {
        errors.remove(error);
        if (lastMaintenance.getValue() == null && !lastMaintenanceContainer.isDisabled()) {
            lastMaintenance.setPromptText(error);
            lastMaintenance.setStyle("-fx-prompt-text-fill: -bgRed;");
            errors.add(error);
        }
    }

    private void checkToggleGroup(ToggleGroup t, String error) {
        errors.remove(error);
        if(t.getSelectedToggle() == null) {
            errors.add(error);
        }
    }

    private void checkDateTimeField(TextField field, String error) {
        if(field.getText().isEmpty() || field.getText().isBlank()) {
            field.setText("00");
        }

        try {
            Integer.parseInt(field.getText());
        } catch (Exception e) {
            errors.add(error);
        }
    }

    private void checkAll() {
        checkTextField(machineCode, "Code is vereist.");
        checkTextField(site, "Site is vereist.");
        checkTextField(machineLoc, "Locatie is vereist.");
        checkTextField(productInfo, "Product info is vereist.");
        checkDate("Datum is vereist.");
        checkToggleGroup(productionStatus, "Productie status is vereist");
        checkToggleGroup(status, "Status is vereist");
        checkDateTimeField(hours, "Uren");
    }

    private void saveMachine() {
        checkAll();
        if (!errors.isEmpty()) {
            //errors tonen
            String errorNotification = "";
            for (String err : errors) {
                errorNotification += err + "\n";
            }
            new Alert(AlertType.ERROR, errorNotification, ButtonType.OK).showAndWait();
        }
        else {
            machine.setCode(machineCode.getText());
            machine.setLocatie(machineLoc.getText());
            machine.setProductInfo(productInfo.getText());
            machine.setUptimeInHours(Integer.parseInt(hours.getText()));

            //set vorige onderhoud
            if(!lastMaintenanceContainer.isDisable()) {
                machine.setTechniekerNaam(technician.getText());
                machine.setLaatsteOnderhoudDatum(lastMaintenance.getValue().atStartOfDay());
            }
            
            //set volgende onderhoud
            if(!(nextMaintenance.getValue() == null)) {
                machine.setDatumToekomstigeOnderhoud(Date.from(nextMaintenance.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
            }
            
            //set status (actief/inactief)
            if(((RadioButton) status.getSelectedToggle()).equals(active)) {
                machine.setCurrentState(new RunningState(machine));
            }
            else {
                machine.setCurrentState(new StoppedState(machine));
            }
            
            //set productie status (gezond / (nood aan) onderhoud / falend)
            machine.setProductieStatus(((RadioButton) status.getSelectedToggle()).getText());
        }
    }
    
}
