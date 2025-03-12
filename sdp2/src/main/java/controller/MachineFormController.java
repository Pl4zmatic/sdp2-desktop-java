package controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import domein.machine.Machine;
import domein.machine.MachineService;
import domein.machine.stateMachines.machine.RunningState;
import domein.machine.stateMachines.machine.StoppedState;
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

    private MachineService machineService;

    @Setter
    private HBox parent;
    @Setter
    ManageMachinesController parentController;

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

    // error checking
    private List<String> errors = new ArrayList<>();

    @FXML
    private void initialize() {
        machineService = new MachineService();
        setupCallbacks();
    }

    private void setupCallbacks() {
        cancel.setOnAction((event) -> cancelCallback());

        machineCode.addEventHandler(MouseEvent.MOUSE_CLICKED, (event) -> editMachineCodeCallback());
        machineCode.focusedProperty().addListener((event) -> checkTextField(machineCode, "Code is vereist."));

        site.focusedProperty().addListener((event) -> checkTextField(site, "Site is vereist."));
        machineLoc.focusedProperty().addListener((event) -> checkTextField(machineLoc, "Locatie is vereist."));
        productInfo.focusedProperty().addListener((event) -> checkTextField(productInfo, "Product info is vereist."));

        technician.textProperty().addListener((event) -> checkTechnician());

        lastMaintenance.setOnAction((event) -> errorInDateFieldLastMaintenance("Datum is vereist."));

        save.setOnAction((event) -> saveMachine());
    }

    private void cancelCallback() {
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

    private boolean errorInDateFieldLastMaintenance(String error) {
        errors.remove(error);
        if (lastMaintenance.getValue() == null && !lastMaintenanceContainer.isDisabled()) {
            lastMaintenance.setPromptText(error);
            lastMaintenance.setStyle("-fx-prompt-text-fill: -bgRed;");
            errors.add(error);
            return true;
        }
        return false;
    }

    private void checkToggleGroup(ToggleGroup t, String error) {
        errors.remove(error);
        if (t.getSelectedToggle() == null) {
            errors.add(error);
        }
    }

    private void checkTimeField(TextField field, String error) {
        if (field.getText().isEmpty() || field.getText().isBlank()) {
            field.setText("00");
        }

        try {
            Integer.parseInt(field.getText());
        } catch (Exception e) {
            errors.add(error);
        }
    }

    private void checkMachineCode(String error) {
        errors.remove(error);
        Set<String> codesInDatabase = machineService.getAllMachines().stream()
                .map((machine) -> machine.getCode())
                .collect(Collectors.toSet());
        if (codesInDatabase.contains(machineCode.getText().trim())) {
            errors.add(error);
        }
    }

    private void validateLastMaintenanceField(String error) {
        errors.remove(error);
        if (lastMaintenance.getValue().isAfter(LocalDate.now())) {
            errors.add(error);
        }
    }

    private void validateNextMaintenanceField(String error) {
        errors.remove(error);
        if (nextMaintenance.getValue() != null) {
            if (nextMaintenance.getValue().isBefore(LocalDate.now())) {
                errors.add(error);
            }
        }
    }

    private void checkAll() {
        checkMachineCode("Code moet uniek zijn.");
        checkTextField(machineCode, "Code is vereist.");
        checkTextField(site, "Site is vereist.");
        checkTextField(machineLoc, "Locatie is vereist.");
        checkTextField(productInfo, "Product info is vereist.");
        checkToggleGroup(productionStatus, "Productie status is vereist");
        checkToggleGroup(status, "Status is vereist");
        checkTimeField(hours, "Uren");

        validateNextMaintenanceField("Datum volgende onderhoud is niet mogelijk.");
        if (!errorInDateFieldLastMaintenance("Datum is vereist.")) {
            validateLastMaintenanceField("Datum laatste onderhoud is niet mogelijk.");
        }
    }

    private void saveMachine() {
        checkAll();
        if (!errors.isEmpty()) {
            // errors tonen
            String errorNotification = "";
            for (String err : errors) {
                errorNotification += err + "\n";
            }
            new Alert(AlertType.ERROR, errorNotification, ButtonType.OK).showAndWait();
        } else {
            if (machine == null)
                machine = new Machine();

            machine.setCode(machineCode.getText());
            machine.setSiteNaam(site.getText());
            machine.setLocatie(machineLoc.getText());
            machine.setProductInfo(productInfo.getText());
            machine.setUptimeInHours(Integer.parseInt(hours.getText()));

            // set vorige onderhoud
            if (!lastMaintenanceContainer.isDisable()) {
                machine.setTechniekerNaam(technician.getText());
                machine.setLaatsteOnderhoudDatum(lastMaintenance.getValue());
            }

            // set volgende onderhoud
            if (!(nextMaintenance.getValue() == null)) {
                machine.setDatumToekomstigeOnderhoud(nextMaintenance.getValue());
            }

            // set status (actief/inactief)
            if (((RadioButton) status.getSelectedToggle()).equals(active)) {
                machine.setCurrentState(new RunningState(machine));
            } else {
                machine.setCurrentState(new StoppedState(machine));
            }
            machine.updateCurrentState();

            // set productie status (gezond / (nood aan) onderhoud / falend)
            machine.setProductieStatus(((RadioButton) productionStatus.getSelectedToggle()).getText());

            machineService.addMachine(machine);
            cancelCallback();
            this.parentController.selectMachine(machine);
        }
    }

    protected void fillFieldData() {
        machineCode.setText(machine.getCode());
        site.setText(machine.getSiteNaam());
        machineLoc.setText(machine.getLocatie());
        productInfo.setText(machine.getProductInfo());
        technician.setText(machine.getTechniekerNaam());
        lastMaintenance.setValue(machine.getLaatsteOnderhoudDatum());
        nextMaintenance.setValue(machine.getDatumToekomstigeOnderhoud());
        hours.setText(String.format("%d", machine.getUptimeInHours()));
        status.selectToggle(machine.getCurrentState().equals("running") ? active : inactive);

        switch (machine.getProductieStatus().toLowerCase()) {
            case "gezond":
                productionStatus.selectToggle(healthy);
                break;
            case "nood aan onderhoud":
                productionStatus.selectToggle(maintenance);
                break;
            case "falend":
                productionStatus.selectToggle(failing);
                break;
        }
    }
}
