package controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Comparator;

import domein.machine.Machine;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import service.MachineService;

// Import MFXTableView and related classes
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
        private MFXTableView<Machine> tableView;

        private ObservableList<Machine> machines;
        private FilteredList<Machine> filteredMachines;
        private MachineService machineService;

        private boolean isTableFiltered;

        @FXML
        private void initialize() {
                machineService = new MachineService();
                isTableFiltered = false;
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

                // Create columns with comparators
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

                // Set row cell factories
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

                // Add columns to the table
                tableView.getTableColumns().addAll(
                        siteColumn, codeColumn, locationColumn, productInfoColumn,
                        statusColumn, productionStatusColumn, uptimeColumn,
                        technicianColumn, lastMaintenanceColumn, nextMaintenanceColumn
                );

                // Configure table appearance
                tableView.setFooterVisible(false);
        }

        private void setupCallbacks() {
                addMachineButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (event) -> addButtonCallback(null));

                // Setup search functionality
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

                // Setup double-click on row
                tableView.setOnMouseClicked(event -> {
                        if (event.getClickCount() == 2) {
                                Machine selectedMachine = tableView.getSelectionModel().getSelectedValues().isEmpty() ?
                                        null : tableView.getSelectionModel().getSelectedValues().get(0);
                                if (selectedMachine != null) {
                                        addButtonCallback(selectedMachine);
                                }
                        }
                });

                rootLayout.getChildren().addListener(new ListChangeListener<Node>() {
                        @Override
                        public void onChanged(Change<? extends Node> c) {
                                loadTableContent();
                        }
                });
        }

        private void loadTableContent() {
                // Get all machines and set up filtered list
                machines = FXCollections.observableArrayList(machineService.getAllMachines());
                filteredMachines = new FilteredList<>(machines, p -> true);

                // Set the items directly instead of clearing first
                tableView.setItems(filteredMachines);
        }

        private void addButtonCallback(Machine m) {
                try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MachineForm.fxml"));
                        Node form = loader.load();
                        MachineFormController controller = ((MachineFormController) loader.getController());
                        controller.setParent(rootLayout);
                        controller.setParentController(this);

                        if (m != null) {
                                controller.setMachine(m);
                                controller.fillFieldData();
                                controller.setupSaveOption();
                        }

                        if (this.rootLayout.getChildren().size() > 2)
                                this.rootLayout.getChildren().removeLast();
                        this.rootLayout.getChildren().add(form);
                } catch (IOException e) {
                        e.printStackTrace();
                }
        }

        protected void selectMachine(Machine m) {
                tableView.getSelectionModel().clearSelection();
                tableView.getSelectionModel().selectItem(m);
        }
}