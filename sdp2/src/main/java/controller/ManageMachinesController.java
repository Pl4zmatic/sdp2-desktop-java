package controller;

import java.io.IOException;
import java.util.Comparator;

import domein.machine.Machine;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.theming.JavaFXThemes;
import io.github.palexdev.materialfx.theming.MaterialFXStylesheets;
import io.github.palexdev.materialfx.theming.UserAgentBuilder;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ManageMachinesController {
    @FXML
    private VBox VboxManageMachines;

    @FXML
    private HBox rootLayout;

    @FXML
    private MFXTableView<Machine> tableView;

    @FXML
    private ImageView addButton;

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
        MFXTableColumn<Machine> nameColumn = new MFXTableColumn<>("Site", true,
                Comparator.comparing(Machine::getSiteNaam));
        MFXTableColumn<Machine> codeColumn = new MFXTableColumn<>("Code", true, Comparator.comparing(Machine::getCode));
        MFXTableColumn<Machine> locationColumn = new MFXTableColumn<>("Locatie", true,
                Comparator.comparing(Machine::getLocatie));
        MFXTableColumn<Machine> productInfoColumn = new MFXTableColumn<>("Product Info", true,
                Comparator.comparing(Machine::getProductInfo));
        MFXTableColumn<Machine> statusColumn = new MFXTableColumn<>("Status", true,
                Comparator.comparing(Machine::getStatus));
        MFXTableColumn<Machine> productionStatusColumn = new MFXTableColumn<>("Productie Status", true,
                Comparator.comparing(Machine::getProductieStatus));
        MFXTableColumn<Machine> uptimeColumn = new MFXTableColumn<>("Uptime", true,
                Comparator.comparing(Machine::getUptime));
        MFXTableColumn<Machine> technicianColumn = new MFXTableColumn<>("Technieker", true,
                Comparator.comparing(Machine::getTechniekerNaam));
        MFXTableColumn<Machine> lastMaintenanceColumn = new MFXTableColumn<>("Laatste Onderhoud", true,
                Comparator.comparing((obj) -> obj.getAantalDagenSindsLaatsteOnderhoud()));
        MFXTableColumn<Machine> nextMaintenanceColumn = new MFXTableColumn<>("Volgend Onderhoud", true,
                Comparator.comparing(Machine::getDatumToekomstigeOnderhoud));

        nameColumn.setRowCellFactory(machine -> new MFXTableRowCell<>(Machine::getSiteNaam));
        codeColumn.setRowCellFactory(machine -> new MFXTableRowCell<>(Machine::getCode));
        locationColumn.setRowCellFactory(machine -> new MFXTableRowCell<>(Machine::getLocatie));
        productInfoColumn.setRowCellFactory(machine -> new MFXTableRowCell<>(Machine::getProductInfo));
        statusColumn.setRowCellFactory(machine -> new MFXTableRowCell<>(Machine::getStatus));
        productionStatusColumn.setRowCellFactory(machine -> new MFXTableRowCell<>(Machine::getProductieStatus));
        uptimeColumn.setRowCellFactory(machine -> new MFXTableRowCell<>(Machine::getUptime));
        technicianColumn.setRowCellFactory(machine -> new MFXTableRowCell<>(Machine::getTechniekerNaam));
        lastMaintenanceColumn.setRowCellFactory(machine -> new MFXTableRowCell<>(Machine::getLaatsteOnderhoud));
        nextMaintenanceColumn
                .setRowCellFactory(machine -> new MFXTableRowCell<>(Machine::getDatumToekomstigeOnderhoud));

        tableView.getTableColumns().addAll(nameColumn, codeColumn, locationColumn, productInfoColumn,
                statusColumn, productionStatusColumn, uptimeColumn, technicianColumn, lastMaintenanceColumn,
                nextMaintenanceColumn);

    }

    private void setupCallbacks() {
        addButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (event) -> addButtonCallback());
    }

    private void addButtonCallback() {
        
    }
}
