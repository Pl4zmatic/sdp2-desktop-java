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
import domein.site.Site;
import domein.user.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import javafx.util.StringConverter;
import lombok.Getter;
import lombok.Setter;
import service.MachineService;
import service.SiteService;
import service.UserService;
import utils.Rollen;

public class MachineFormController {

    @Getter
    @Setter
    private Machine machine;

    private MachineService machineService;
    private SiteService siteService;
    private UserService userService;

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

    @FXML
    private VBox machineCodeContainer;
    @FXML
    private TextField machineCode;
    @FXML
    private ComboBox<Site> siteComboBox;
    @FXML
    private TextField machineLoc;
    @FXML
    private ComboBox<User> technicianComboBox;
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

    @FXML
    private TextField days;
    @FXML
    private TextField hours;
    @FXML
    private TextField minutes;

    @FXML
    private RadioButton active;
    @FXML
    private RadioButton inactive;
    @FXML
    private ToggleGroup status;

    @FXML
    private RadioButton failing;
    @FXML
    private RadioButton maintenance;
    @FXML
    private RadioButton healthy;
    @FXML
    private ToggleGroup productionStatus;

    @FXML
    private Button save;
    @FXML
    private Button cancel;

    private Runnable onSaveCallback;
    private EventHandler<ActionEvent> closeHandler;

    private List<String> errors = new ArrayList<>();

    private boolean isEditingFlag;
    private FilteredList<Site> filteredSites;
    private FilteredList<User> filteredTechnicians;

    @FXML
    private void initialize() {
        machineService = new MachineService();
        siteService = SiteService.getInstance();
        userService = UserService.getInstance();
        setupCallbacks();
        setupScrollPane();
        setupSiteComboBox();
        setupTechnicianComboBox();
        isEditingFlag = false;

        if (formTitle != null) {
            formTitle.setText("Machine Form");
        }
    }

    private void setupTechnicianComboBox() {
        List<User> allTechnicians = userService.getAllActiveUsers().stream()
                .filter(user -> user.getRol() == Rollen.TECHNIEKER)
                .collect(Collectors.toList());

        if (allTechnicians.isEmpty()) {
            technicianComboBox.setDisable(true);
            technicianComboBox.setPromptText("No technicians available");
            return;
        }

        ObservableList<User> technicians = FXCollections.observableArrayList(allTechnicians);

        filteredTechnicians = new FilteredList<>(technicians, p -> true);

        technicianComboBox.setItems(filteredTechnicians);

        technicianComboBox.getStyleClass().add("comboBox");
        technicianComboBox.getStyleClass().add("filter-combo");

        technicianComboBox.setCellFactory(new Callback<ListView<User>, ListCell<User>>() {
            @Override
            public ListCell<User> call(ListView<User> param) {
                return new ListCell<User>() {
                    @Override
                    protected void updateItem(User item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setText(null);
                        } else {
                            setText(item.getFirstName() + " " + item.getLastName());
                        }
                    }
                };
            }
        });

        technicianComboBox.setConverter(new StringConverter<User>() {
            @Override
            public String toString(User user) {
                return user == null ? "" : user.getFirstName() + " " + user.getLastName();
            }

            @Override
            public User fromString(String string) {
                if (string == null || string.isEmpty()) {
                    return null;
                }
                return technicians.stream()
                        .filter(user -> (user.getFirstName() + " " + user.getLastName()).equalsIgnoreCase(string))
                        .findFirst()
                        .orElse(null);
            }
        });

        technicianComboBox.setEditable(true);

        TextField editor = technicianComboBox.getEditor();

        final boolean[] isUpdatingFilter = new boolean[1];

        editor.textProperty().addListener((observable, oldValue, newValue) -> {
            if (isUpdatingFilter[0]) {
                return;
            }

            isUpdatingFilter[0] = true;
            try {
                filteredTechnicians.setPredicate(user -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }

                    String lowerCaseFilter = newValue.toLowerCase();
                    String fullName = user.getFirstName() + " " + user.getLastName();
                    return fullName.toLowerCase().contains(lowerCaseFilter);
                });

                if (filteredTechnicians.size() > 0 && !newValue.isEmpty()) {
                    if (!technicianComboBox.isShowing()) {
                        technicianComboBox.show();
                    }
                } else if (technicianComboBox.isShowing() && filteredTechnicians.isEmpty()) {
                    technicianComboBox.hide();
                }
            } finally {
                isUpdatingFilter[0] = false;
            }
        });

        technicianComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (isUpdatingFilter[0] || newVal == null) {
                return;
            }

            isUpdatingFilter[0] = true;
            try {
                editor.setText(newVal.getFirstName() + " " + newVal.getLastName());
                editor.positionCaret(editor.getText().length());
                editor.setStyle("-fx-text-fill: -deepBlue; -fx-font-weight: normal;");

                lastMaintenanceContainer.setDisable(false);
                if (machine == null) {
                    details.setDisable(true);
                }
            } finally {
                isUpdatingFilter[0] = false;
            }
        });

        editor.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.DOWN ||
                    event.getCode() == KeyCode.UP ||
                    event.getCode() == KeyCode.ENTER) {
                return;
            }
        });

        technicianComboBox.setPromptText("Select or type to search");
    }
    private void setupSiteComboBox() {
        List<Site> allSites = siteService.getAllSites().stream()
                .filter(site -> !site.getDeleted())
                .collect(Collectors.toList());

        if (allSites.isEmpty()) {
            siteComboBox.setDisable(true);
            siteComboBox.setPromptText("No sites available");
            return;
        }

        ObservableList<Site> sites = FXCollections.observableArrayList(allSites);

        filteredSites = new FilteredList<>(sites, p -> true);

        siteComboBox.setItems(filteredSites);

        siteComboBox.getStyleClass().add("comboBox");
        siteComboBox.getStyleClass().add("filter-combo");

        siteComboBox.setCellFactory(new Callback<ListView<Site>, ListCell<Site>>() {
            @Override
            public ListCell<Site> call(ListView<Site> param) {
                return new ListCell<Site>() {
                    @Override
                    protected void updateItem(Site item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setText(null);
                        } else {
                            setText(item.getName());
                        }
                    }
                };
            }
        });

        siteComboBox.setConverter(new StringConverter<Site>() {
            @Override
            public String toString(Site site) {
                return site == null ? "" : site.getName();
            }

            @Override
            public Site fromString(String string) {
                if (string == null || string.isEmpty()) {
                    return null;
                }
                return sites.stream()
                        .filter(site -> site.getName().equalsIgnoreCase(string))
                        .findFirst()
                        .orElse(null);
            }
        });

        siteComboBox.setEditable(true);

        TextField editor = siteComboBox.getEditor();

        final boolean[] isUpdatingFilter = new boolean[1];

        editor.textProperty().addListener((observable, oldValue, newValue) -> {
            if (isUpdatingFilter[0]) {
                return;
            }

            isUpdatingFilter[0] = true;
            try {
                filteredSites.setPredicate(site -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }

                    String lowerCaseFilter = newValue.toLowerCase();
                    return site.getName().toLowerCase().contains(lowerCaseFilter);
                });

                if (filteredSites.size() > 0 && !newValue.isEmpty()) {
                    if (!siteComboBox.isShowing()) {
                        siteComboBox.show();
                    }
                } else if (siteComboBox.isShowing() && filteredSites.isEmpty()) {
                    siteComboBox.hide();
                }
            } finally {
                isUpdatingFilter[0] = false;
            }
        });

        siteComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (isUpdatingFilter[0] || newVal == null) {
                return;
            }

            isUpdatingFilter[0] = true;
            try {
                editor.setText(newVal.getName());
                editor.positionCaret(editor.getText().length());
                editor.setStyle("-fx-text-fill: -deepBlue; -fx-font-weight: normal;");
            } finally {
                isUpdatingFilter[0] = false;
            }
        });

        editor.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.DOWN ||
                    event.getCode() == KeyCode.UP ||
                    event.getCode() == KeyCode.ENTER) {
                return;
            }
        });

        siteComboBox.setPromptText("Select or type to search");

        if (machine != null && machine.getSite() != null) {
            for (Site site : sites) {
                if (site.getId() == machine.getSite().getId()) {
                    siteComboBox.setValue(site);
                    break;
                }
            }
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
        machineCode.addEventHandler(MouseEvent.MOUSE_CLICKED, (event) -> editMachineCodeCallback());
        machineCode.focusedProperty().addListener((event) -> checkTextField(machineCode, "Code is vereist."));

        siteComboBox.focusedProperty().addListener((event) -> checkSiteComboBox("Site is vereist."));

        machineLoc.focusedProperty().addListener((event) -> checkTextField(machineLoc, "Locatie is vereist."));
        productInfo.focusedProperty().addListener((event) -> checkTextField(productInfo, "Product info is vereist."));

        technicianComboBox.focusedProperty().addListener((event) -> checkTechnicianComboBox("Technieker is vereist."));

        lastMaintenance.setOnAction((event) -> errorInDateFieldLastMaintenance("Datum is vereist."));

        save.setOnAction((event) -> saveMachine());
    }

    private void checkTechnicianComboBox(String error) {
        errors.remove(error);
        if (technicianComboBox.getValue() == null && !technicianComboBox.isFocused()) {
            technicianComboBox.setPromptText(error);
            technicianComboBox.setStyle("-fx-prompt-text-fill: -bgRed;");
            errors.add(error);
        }
    }

    private void checkSiteComboBox(String error) {
        errors.remove(error);
        if (siteComboBox.getValue() == null && !siteComboBox.isFocused()) {
            siteComboBox.setPromptText(error);
            siteComboBox.setStyle("-fx-prompt-text-fill: -bgRed;");
            errors.add(error);
        }
    }

    private void cancelCallback() {
        if (closeHandler != null) {
            closeHandler.handle(new ActionEvent());
        }

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
        if (technicianComboBox.getValue() != null) {
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
            checkMachineCode("Code must be unique.");
        }

        checkTextField(machineCode, "Code is mandatory.");
        checkSiteComboBox("Plant is mandatory.");
        checkTextField(machineLoc, "Location is mandatory.");
        checkTextField(productInfo, "Product info is mandatory.");
        checkToggleGroup(productionStatus, "Production status is mandatory.");
        checkToggleGroup(status, "Status is mandatory.");
        checkTimeField(hours, "Uren");
        checkTechnicianComboBox("Technician is mandatory");

        validateNextMaintenanceField("Date last maintenance is not possible.");
        if (!errorInDateFieldLastMaintenance("Date is mandatory.")) {
            validateLastMaintenanceField("Date last maintenance is not possible.");
        }
    }

    private void saveMachine() {
        checkAll();
        if (!errors.isEmpty()) {
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

            Site selectedSite = siteComboBox.getValue();
            if (selectedSite != null) {
                machine.setSite(selectedSite);
            } else {
                new Alert(AlertType.ERROR, "Site is vereist.", ButtonType.OK).showAndWait();
                return;
            }

            machine.setLocatie(machineLoc.getText());
            machine.setProductInfo(productInfo.getText());
            machine.setUptimeInHours(Integer.parseInt(hours.getText()));

            User selectedTechnician = technicianComboBox.getValue();
            if (selectedTechnician != null) {
                machine.setTechniekerNaam(selectedTechnician.getFirstName() + " " + selectedTechnician.getLastName());
                machine.setLaatsteOnderhoudDatum(lastMaintenance.getValue());
            }

            if (!(nextMaintenance.getValue() == null)) {
                machine.setDatumToekomstigeOnderhoud(nextMaintenance.getValue());
            }

            if (((RadioButton) status.getSelectedToggle()).equals(active)) {
                machine.setCurrentState(new RunningState(machine));
            } else {
                machine.setCurrentState(new StoppedState(machine));
            }
            machine.updateCurrentState();

            machine.setProductieStatus(((RadioButton) productionStatus.getSelectedToggle()).getText());

            try {
                boolean isDeleted = ((RadioButton) status.getSelectedToggle()).equals(inactive);
                machine.setDeleted(isDeleted);
            } catch (Exception e) {
                System.out.println("Machine doesn't have setDeleted method, skipping");
            }

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
            siteComboBox.setValue(machine.getSite());
            machineLoc.setText(machine.getLocatie());
            productInfo.setText(machine.getProductInfo());

            String technicianName = machine.getTechniekerNaam();
            if (technicianName != null && !technicianName.isEmpty()) {
                for (User technician : filteredTechnicians) {
                    String fullName = technician.getFirstName() + " " + technician.getLastName();
                    if (fullName.equals(technicianName)) {
                        technicianComboBox.setValue(technician);
                        break;
                    }
                }

                if (technicianComboBox.getValue() == null) {
                    technicianComboBox.getEditor().setText(technicianName);
                }
            }

            lastMaintenance.setValue(machine.getLaatsteOnderhoudDatum());
            nextMaintenance.setValue(machine.getDatumToekomstigeOnderhoud());
            hours.setText(String.format("%d", machine.getUptimeInHours()));

            if (days.getText().isEmpty()) days.setText("00");
            if (minutes.getText().isEmpty()) minutes.setText("00");

            if (machine.getCurrentState().equals("running")) {
                active.setSelected(true);
                inactive.setSelected(false);
            } else {
                active.setSelected(false);
                inactive.setSelected(true);
            }

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
            System.out.println("IS AANGEROEPEN");
            machineCode.setText("");
            siteComboBox.setValue(null);
            machineLoc.setText("");
            productInfo.setText("");
            technicianComboBox.setValue(null);
            lastMaintenance.setValue(LocalDate.now());
            nextMaintenance.setValue(null);
            hours.setText("0");
            days.setText("00");
            minutes.setText("00");

            active.setSelected(true);
            healthy.setSelected(true);
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