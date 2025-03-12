package controller;
import java.io.IOException;
import java.util.Comparator;
import domein.machine.Machine;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import service.MachineService;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;

public class ManageMachinesController {
        @FXML
        private BorderPane rootLayout;

        @FXML
        private TextField searchBar;

        @FXML
        private Button addMachineButton;

        @FXML
        private CheckBox showDeletedMachines;

        @FXML
        private MFXTableView<Machine> tableView;

        private ObservableList<Machine> machines;
        private FilteredList<Machine> filteredMachines;
        private MachineService machineService;

        @FXML
        private void initialize() {
                machineService = new MachineService();
                try {
                        setupTable();
                        setupCallbacks();
                        setupNavbar();
                        loadTableContent();
                } catch (Exception e) {
                        e.printStackTrace();
                }
        }

        private void setupNavbar() throws IOException {
                this.rootLayout.setLeft(NavbarManager.getNavbar());
        }

        private void setupTable() {
                tableView.getTableColumns().clear();

                MFXTableColumn<Machine> siteColumn = new MFXTableColumn<>("Site", true, Comparator.comparing(Machine::getSiteNaam));
                MFXTableColumn<Machine> codeColumn = new MFXTableColumn<>("Code", true, Comparator.comparing(Machine::getCode));
                MFXTableColumn<Machine> locationColumn = new MFXTableColumn<>("Location", true, Comparator.comparing(Machine::getLocatie));
                MFXTableColumn<Machine> productInfoColumn = new MFXTableColumn<>("Product Info", true, Comparator.comparing(Machine::getProductInfo));
                MFXTableColumn<Machine> statusColumn = new MFXTableColumn<>("Status", true, Comparator.comparing(Machine::getCurrentStateString));
                MFXTableColumn<Machine> productionStatusColumn = new MFXTableColumn<>("Production Status", true, Comparator.comparing(Machine::getProductieStatus));
                MFXTableColumn<Machine> uptimeColumn = new MFXTableColumn<>("Uptime (hours)", true, Comparator.comparing(Machine::getUptimeInHours));
                MFXTableColumn<Machine> technicianColumn = new MFXTableColumn<>("Technician", true, Comparator.comparing(Machine::getTechniekerNaam));
                MFXTableColumn<Machine> lastMaintenanceColumn = new MFXTableColumn<>("Last Maintenance", true, Comparator.comparing(Machine::getLaatsteOnderhoudDatum));
                MFXTableColumn<Machine> nextMaintenanceColumn = new MFXTableColumn<>("Next Maintenance", true, Comparator.comparing(Machine::getDatumToekomstigeOnderhoud));
                MFXTableColumn<Machine> actionsColumn = new MFXTableColumn<>("Actions", true);

                siteColumn.setRowCellFactory(machine -> new MFXTableRowCell<>(Machine::getSiteNaam));
                codeColumn.setRowCellFactory(machine -> new MFXTableRowCell<>(Machine::getCode));
                locationColumn.setRowCellFactory(machine -> new MFXTableRowCell<>(Machine::getLocatie));
                productInfoColumn.setRowCellFactory(machine -> new MFXTableRowCell<>(Machine::getProductInfo));
                statusColumn.setRowCellFactory(machine -> new MFXTableRowCell<>(Machine::getCurrentStateString));
                productionStatusColumn.setRowCellFactory(machine -> new MFXTableRowCell<>(Machine::getProductieStatus));
                uptimeColumn.setRowCellFactory(machine -> new MFXTableRowCell<>(Machine::getUptimeInHours));
                technicianColumn.setRowCellFactory(machine -> new MFXTableRowCell<>(Machine::getTechniekerNaam));
                lastMaintenanceColumn.setRowCellFactory(machine -> new MFXTableRowCell<>(Machine::getLaatsteOnderhoudDatum));
                nextMaintenanceColumn.setRowCellFactory(machine -> new MFXTableRowCell<>(Machine::getDatumToekomstigeOnderhoud));

                MFXTableColumn<Machine> activeStatusColumn = new MFXTableColumn<>("Active Status", true, Comparator.comparing(machine -> !machine.getDeleted()));
                activeStatusColumn.setRowCellFactory(machine -> new MFXTableRowCell<>(m -> m.getDeleted() ? "Inactive" : "Active"));

                actionsColumn.setRowCellFactory(machine -> {
                        MFXTableRowCell<Machine, String> cell = new MFXTableRowCell<>(m -> "");

                        final String machineCode = machine.getCode();

                        HBox hbox = new HBox(10);
                        hbox.setAlignment(Pos.CENTER);
                        hbox.getStyleClass().add("actions-container");
                        hbox.setMinHeight(40);
                        hbox.setPrefHeight(40);

                        Button editButton = new Button("Edit");
                        editButton.getStyleClass().add("edit-button");
                        editButton.setMaxWidth(Double.MAX_VALUE);
                        editButton.setOnAction(event -> {
                                Machine selectedMachine = findMachineByCode(machineCode);
                                if (selectedMachine != null) {
                                        editMachine(selectedMachine);
                                } else {
                                        System.out.println("ERROR: Could not find machine with code: " + machineCode);
                                }
                        });

                        Button deleteButton = new Button("Delete");
                        deleteButton.getStyleClass().add("delete-button");
                        deleteButton.setMaxWidth(Double.MAX_VALUE);
                        deleteButton.setOnAction(event -> {
                                Machine selectedMachine = findMachineByCode(machineCode);
                                if (selectedMachine != null) {
                                        deleteMachine(selectedMachine);
                                } else {
                                        System.out.println("ERROR: Could not find machine with code: " + machineCode);
                                }
                        });

                        hbox.getChildren().addAll(editButton, deleteButton);
                        cell.setGraphic(hbox);
                        cell.setAlignment(Pos.CENTER);

                        return cell;
                });

                actionsColumn.setPrefWidth(160);
                actionsColumn.setMinWidth(160);

                tableView.getTableColumns().addAll(
                        siteColumn, codeColumn, locationColumn, productInfoColumn,
                        statusColumn, productionStatusColumn, uptimeColumn,
                        technicianColumn, lastMaintenanceColumn, nextMaintenanceColumn,
                        activeStatusColumn, actionsColumn
                );

                tableView.setFooterVisible(false);
        }

        private Machine findMachineByCode(String code) {
                for (Machine machine : machines) {
                        if (machine.getCode().equals(code)) {
                                return machine;
                        }
                }
                return null;
        }

        private void deleteMachine(Machine machine) {
                Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION,
                        "Are you sure you want to delete machine " + machine.getCode() + "?",
                        ButtonType.YES, ButtonType.NO);
                confirmDialog.setTitle("Confirm Delete");
                confirmDialog.setHeaderText("Delete Machine");

                confirmDialog.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.YES) {
                                boolean success = machineService.deleteMachine(machine.getCode());
                                if (success) {
                                        refreshTable();
                                } else {
                                        Alert errorAlert = new Alert(Alert.AlertType.ERROR,
                                                "Failed to delete machine with code: " + machine.getCode(),
                                                ButtonType.OK);
                                        errorAlert.setTitle("Error");
                                        errorAlert.setHeaderText("Delete Failed");
                                        errorAlert.showAndWait();
                                }
                        }
                });
        }

        private void editMachine(Machine machine) {
                try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MachineForm.fxml"));
                        Parent root = loader.load();

                        MachineFormController controller = loader.getController();
                        controller.setMachine(machine);
                        controller.setupSaveOption();
                        controller.fillFieldData();

                        Scene scene = new Scene(root);
                        SceneSwitcher.getStage().setScene(scene);
                } catch (IOException e) {
                        e.printStackTrace();
                }
        }

        private void setupCallbacks() {
                addMachineButton.setOnAction(event -> addButtonCallback());

                showDeletedMachines.selectedProperty().addListener((observable, oldValue, newValue) -> {
                        loadTableContent();
                });

                searchBar.textProperty().addListener((observable, oldValue, newValue) -> {
                        if (filteredMachines != null) {
                                filteredMachines.setPredicate(machine -> {
                                        if (newValue == null || newValue.isEmpty()) {
                                                return true;
                                        }
                                        String lowerCaseFilter = newValue.toLowerCase();
                                        return machine.getSiteNaam().toLowerCase().contains(lowerCaseFilter) ||
                                                machine.getCode().toLowerCase().contains(lowerCaseFilter) ||
                                                machine.getLocatie().toLowerCase().contains(lowerCaseFilter);
                                });
                        }
                });

                tableView.setOnMouseClicked(event -> {
                        if (event.getClickCount() == 2) {
                                Machine selectedMachine = tableView.getSelectionModel().getSelectedValues().isEmpty() ?
                                        null : tableView.getSelectionModel().getSelectedValues().get(0);
                                if (selectedMachine != null) {
                                        editMachine(selectedMachine);
                                }
                        }
                });
        }

        private void loadTableContent() {
                if (machines != null) {
                        machines.clear();
                }

                if (showDeletedMachines.isSelected()) {
                        machines = FXCollections.observableArrayList(machineService.getAllMachines());
                } else {
                        machines = FXCollections.observableArrayList(machineService.getAllActiveMachines());
                }

                filteredMachines = new FilteredList<>(machines, p -> true);

                tableView.getItems().clear();
                tableView.setItems(filteredMachines);
        }

        private void addButtonCallback() {
                try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MachineForm.fxml"));
                        Parent root = loader.load();

                        MachineFormController controller = loader.getController();
                        controller.setMachine(null);
                        controller.setupSaveOption();

                        Scene scene = new Scene(root);
                        SceneSwitcher.getStage().setScene(scene);
                } catch (IOException e) {
                        e.printStackTrace();
                }
        }

        protected void selectMachine(Machine m) {
                tableView.getSelectionModel().clearSelection();
                tableView.getSelectionModel().selectItem(m);
        }

        public void refreshTable() {
                loadTableContent();
        }
}

