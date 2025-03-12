package controller;

import java.io.IOException;
import java.time.LocalDate;

import domein.machine.Machine;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import service.MachineService;

public class ManageMachinesController {
        @FXML
        private HBox rootLayout;

        @FXML
        private HBox manageMachinesContainer;

        @FXML
        private VBox VboxManageMachines;

        @FXML
        private TextField searchBar;

        @FXML
        private ImageView addButton;

        @FXML
        private TableView<Machine> tableView;
        @FXML
        private TableColumn<Machine, String> siteColumn;
        @FXML
        private TableColumn<Machine, String> codeColumn;
        @FXML
        private TableColumn<Machine, String> locationColumn;
        @FXML
        private TableColumn<Machine, String> productInfoColumn;
        @FXML
        private TableColumn<Machine, String> productionStatusColumn;
        @FXML
        private TableColumn<Machine, String> statusColumn;
        @FXML
        private TableColumn<Machine, String> technicianColumn;
        @FXML
        private TableColumn<Machine, Integer> uptimeColumn;
        @FXML
        private TableColumn<Machine, LocalDate> lastMaintenanceColumn;
        @FXML
        private TableColumn<Machine, LocalDate> nextMaintenanceColumn;

        private MachineService machineService;

        @FXML
        private void initialize() {
                machineService = new MachineService();
                try {
                        setupTable();
                        setupCallbacks();
                        setupNavbar();
                } catch (Exception e) {
                        e.printStackTrace();
                }
        }

        private void setupNavbar() throws IOException {
                this.rootLayout.getChildren().add(0, NavbarManager.getNavbar());
        }

        private void setupTable() {
                siteColumn.setCellValueFactory(new PropertyValueFactory<>("siteNaam"));
                codeColumn.setCellValueFactory(new PropertyValueFactory<>("code"));
                locationColumn.setCellValueFactory(new PropertyValueFactory<>("locatie"));
                productInfoColumn.setCellValueFactory(new PropertyValueFactory<>("productInfo"));
                statusColumn.setCellValueFactory(new PropertyValueFactory<>("currentStateString"));
                productionStatusColumn.setCellValueFactory(new PropertyValueFactory<>("productieStatus"));
                uptimeColumn.setCellValueFactory(new PropertyValueFactory<>("uptimeInHours"));
                technicianColumn.setCellValueFactory(new PropertyValueFactory<>("techniekerNaam"));
                lastMaintenanceColumn.setCellValueFactory(new PropertyValueFactory<>("laatsteOnderhoudDatum"));
                nextMaintenanceColumn.setCellValueFactory(new PropertyValueFactory<>("datumToekomstigeOnderhoud"));
        }

        private void setupCallbacks() {
                addButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (event) -> addButtonCallback(null));
                searchBar.setOnAction((event) -> searchBarCallback());
                rootLayout.getChildren().addListener(new ListChangeListener<Node>() {
                        @Override
                        public void onChanged(Change<? extends Node> c) {
                                loadTableContent();
                        }
                });
                tableView.setRowFactory(table -> {
                        TableRow<Machine> row = new TableRow<>();
                        row.setOnMouseClicked(event -> {
                                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                                        Machine rowData = row.getItem();
                                        addButtonCallback(rowData);
                                }
                        });
                        return row;
                });
        }

        private void loadTableContent() {
                this.tableView.setItems(FXCollections.observableList(machineService.getAllMachines()));
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
                        }

                        if (this.rootLayout.getChildren().size() > 2)
                                this.rootLayout.getChildren().removeLast();
                        this.rootLayout.getChildren().add(form);
                } catch (IOException e) {
                        e.printStackTrace();
                }
        }

        private void searchBarCallback() {

        }

        protected void selectMachine(Machine m) {
                this.tableView.getSelectionModel().select(m);
        }
}
