package controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;

import com.mysql.cj.conf.ConnectionUrlParser.Pair;

import domein.machine.Machine;
import domein.machine.MachineService;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

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
        private TableColumn<Machine, LocalDateTime> uptimeColumn;
        @FXML
        private TableColumn<Machine, Pair<LocalDateTime, String>> lastMaintenanceColumn;
        @FXML
        private TableColumn<Machine, Date> nextMaintenanceColumn;

        private MachineService machineService;

        @FXML
        private void initialize() {
                machineService = new MachineService();
                try {
                        setupNavbar();
                        setupTable();
                        setupCallbacks();
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
                statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
                productionStatusColumn.setCellValueFactory(new PropertyValueFactory<>("productieStatus"));
                uptimeColumn.setCellValueFactory(new PropertyValueFactory<>("uptime"));
                technicianColumn.setCellValueFactory(new PropertyValueFactory<>("techniekerNaam"));
                lastMaintenanceColumn.setCellValueFactory(new PropertyValueFactory<>("laatsteOnderhoud"));
                lastMaintenanceColumn.setCellValueFactory(new PropertyValueFactory<>("datumToekomstigeOnderhoud"));
                loadTableContent();
        }

        private void setupCallbacks() {
                addButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (event) -> addButtonCallback());
                searchBar.setOnAction((event) -> searchBarCallback());
                rootLayout.getChildren().addListener(new ListChangeListener<Node>() {
                        @Override
                        public void onChanged(Change<? extends Node> c) {
                                loadTableContent();
                        }
                });
        }

        private void loadTableContent() {
                this.tableView.setItems(FXCollections.observableList(machineService.getAllMachines()));
        }

        private void addButtonCallback() {
                try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MachineForm.fxml"));
                        Node form = loader.load();
                        ((MachineFormController) loader.getController()).setParent(rootLayout);
                        this.rootLayout.getChildren().add(form);
                } catch (IOException e) {
                        e.printStackTrace();
                }
        }

        private void searchBarCallback() {

        }
}
