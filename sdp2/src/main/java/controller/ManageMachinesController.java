package controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;

import com.mysql.cj.conf.ConnectionUrlParser.Pair;

import domein.machine.Machine;
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

        @FXML
        private void initialize() {
                try {
                        setupNavbar();
                        setupTable();
                        setupCallbacks();
                } catch (Exception e) {
                        e.printStackTrace();
                }
        }

        private void setupNavbar() throws IOException {
                Node sideBar = new FXMLLoader(getClass().getResource("/view/Navbar.fxml")).load();
                rootLayout.getChildren().add(0, sideBar);
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
        }

        private void setupCallbacks() {
                addButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (event) -> addButtonCallback());
        }

        private void addButtonCallback() {

        }
}
