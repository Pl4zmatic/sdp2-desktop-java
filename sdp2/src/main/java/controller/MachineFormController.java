package controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import domein.machine.Machine;
import domein.machine.stateMachines.machine.RunningState;
import domein.machine.stateMachines.machine.StoppedState;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import lombok.Getter;
import lombok.Setter;
import service.MachineService;

public class MachineFormController {

    @Getter
    @Setter
    private Machine machine;

    private MachineService machineService;

    @Setter
    private BorderPane parent;
    @Setter
    ManageMachinesController parentController;

    @FXML
    private BorderPane rootLayout;

    @FXML
    private VBox contentVBox;

    @FXML
    private Label formTitle;

    // text fields
    @FXML
    private VBox machineCodeContainer;
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

    // Nieuwe velden voor het rechterpaneel
    private Runnable onSaveCallback;
    private EventHandler<ActionEvent> closeHandler;

    // error checking
    private List<String> errors = new ArrayList<>();

    private boolean isEditingFlag;

    @FXML
    private void initialize() {
        // Verwijder de navbar setup omdat we in een zijpaneel zitten
        // rootLayout.setLeft(NavbarManager.getNavbar());
        machineService = new MachineService();
        setupCallbacks();
        setupScrollPane();
        isEditingFlag = false;

        // Set default form title
        if (formTitle != null) {
            formTitle.setText("Machine Form");
        }
    }

    public void setupSaveOption() {
        if(machine != null) {
            save.setText("Update");
            isEditingFlag = true;
            machineCodeContainer.setDisable(true);
            formTitle.setText("Edit Machine");
        }
        else {
            save.setText("Add");
            isEditingFlag = false;
            machineCodeContainer.setDisable(false);
            formTitle.setText("Add Machine");
        }
    }

    private void setupScrollPane() {
        if (rootLayout != null) {
            rootLayout.getStyleClass().add("edge-to-edge");
        }
    }

    private void setupCallbacks() {
        // De cancel-knop actie wordt nu in de addCloseHandler methode ingesteld

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
        // Roep de closeHandler aan als deze is ingesteld
        if (closeHandler != null) {
            closeHandler.handle(new ActionEvent());
        }

        // Roep ook de callback aan voor consistentie
        if (onSaveCallback != null) {
            onSaveCallback.run();
        }
    }

    private void editMachineCodeCallback() {
        if (!isEditingFlag) {
            machineCode.setStyle("-fx-background-color: white;");
            machineCode.setText("");
        }
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
        if (!isEditingFlag) {
            Set<String> codesInDatabase = machineService.getAllMachines().stream()
                    .map((machine) -> machine.getCode())
                    .collect(Collectors.toSet());
            if (codesInDatabase.contains(machineCode.getText().trim())) {
                errors.add(error);
            }
        }
    }

    private void validateLastMaintenanceField(String error) {
        errors.remove(error);
        if(lastMaintenance.getValue() != null) {
            if (lastMaintenance.getValue().isAfter(LocalDate.now())) {
                errors.add(error);
            }
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
        if(!isEditingFlag) {
            checkMachineCode("Code moet uniek zijn.");
        }

        checkTextField(machineCode, "Code is vereist.");
        checkTextField(site, "Site is vereist.");
        checkTextField(machineLoc, "Locatie is vereist.");
        checkTextField(productInfo, "Product info is vereist.");
        checkToggleGroup(productionStatus, "Productie status is vereist.");
        checkToggleGroup(status, "Status is vereist.");
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
            if (machine == null) {
                machine = new Machine();
            }

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

            // Set deleted status based on active/inactive selection
            try {
                boolean isDeleted = ((RadioButton) status.getSelectedToggle()).equals(inactive);
                machine.setDeleted(isDeleted);
            } catch (Exception e) {
                // If setDeleted method doesn't exist, we'll continue without setting it
                System.out.println("Machine doesn't have setDeleted method, skipping");
            }

            // Save or update the machine
            boolean success;
            if (isEditingFlag) {
                success = machineService.update(machine);
            } else {
                success = machineService.addMachine(machine);
            }

            if (success) {
                String message = isEditingFlag ? "Machine successfully updated!" : "Machine successfully created!";
                new Alert(AlertType.INFORMATION, message, ButtonType.OK).showAndWait();

                // Roep de callback aan in plaats van naar een ander scherm te gaan
                if (onSaveCallback != null) {
                    onSaveCallback.run();
                }
            } else {
                new Alert(AlertType.ERROR, "Failed to save machine.", ButtonType.OK).showAndWait();
            }
        }
    }

    public void fillFieldData() {
        if (machine != null) {
            machineCode.setText(machine.getCode());
            site.setText(machine.getSiteNaam());
            machineLoc.setText(machine.getLocatie());
            productInfo.setText(machine.getProductInfo());
            technician.setText(machine.getTechniekerNaam());
            lastMaintenance.setValue(machine.getLaatsteOnderhoudDatum());
            nextMaintenance.setValue(machine.getDatumToekomstigeOnderhoud());
            hours.setText(String.format("%d", machine.getUptimeInHours()));

            if (days.getText().isEmpty()) days.setText("00");
            if (minutes.getText().isEmpty()) minutes.setText("00");

            // Set status based on machine's current state
            if (machine.getCurrentState().equals("running")) {
                active.setSelected(true);
                inactive.setSelected(false);
            } else {
                active.setSelected(false);
                inactive.setSelected(true);
            }

            // Set production status
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
                boolean isDeleted = machine.getDeleted();
                if (isDeleted) {
                    inactive.setSelected(true);
                    active.setSelected(false);
                } else {
                    active.setSelected(true);
                    inactive.setSelected(false);
                }
        } else {
            // Clear fields
            machineCode.setText("");
            site.setText("");
            machineLoc.setText("");
            productInfo.setText("");
            technician.setText("");
            lastMaintenance.setValue(null);
            nextMaintenance.setValue(null);
            hours.setText("0");
            days.setText("00");
            minutes.setText("00");

            // Set default values
            if (active != null) active.setSelected(true);
            if (healthy != null) healthy.setSelected(true);
        }
    }
    public void addCloseButton(EventHandler<ActionEvent> closeHandler) {
        this.closeHandler = closeHandler;

        if (cancel != null) {
            cancel.setOnAction(event -> cancelCallback());

        }
    }

    public void setOnSaveCallback(Runnable callback) {
        this.onSaveCallback = callback;
    }
}