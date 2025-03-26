package controller;

import domein.Session;
import domein.machine.Machine;
import domein.site.Site;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import service.ServiceController;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class OverviewPlantsController {
    @FXML
    private BorderPane rootLayout;

    @FXML
    private Button backButton;

    @FXML
    private Label siteNameLabel;

    @FXML
    private TextField searchBar;

    @FXML
    private ComboBox<String> statusFilterComboBox;

    @FXML
    private FlowPane machineCardsContainer;

    private ObservableList<Machine> machines;
    private FilteredList<Machine> filteredMachines;
    private ServiceController sc;
    private Site currentSite;

    @FXML
    private void initialize() throws IOException {
        sc = ServiceController.getInstance();
        setupNavbar();
        loadCurrentSite();
        setupStatusFilter();
        loadMachineCards();
        setupSearchFilter();
    }

    @FXML
    private void handleBackButton() {
        SceneSwitcher.switchScene("/view/SiteSelectionPlantOverview.fxml");
    }

    private void setupNavbar() throws IOException {
        this.rootLayout.setLeft(NavbarManager.getNavbar());
    }

    private void loadCurrentSite() {
        currentSite = Session.getCurrentSite();
        if (currentSite != null) {
            siteNameLabel.setText(currentSite.getName());
        }
    }

    private void setupStatusFilter() {
        List<String> statuses = Arrays.asList("All Statuses", "running", "startable", "stopped");
        statusFilterComboBox.setItems(FXCollections.observableArrayList(statuses));
        statusFilterComboBox.getSelectionModel().selectFirst();

        statusFilterComboBox.setOnAction(event -> applyFilters());
    }

    private void setupSearchFilter() {
        searchBar.textProperty().addListener((observable, oldValue, newValue) -> applyFilters());
    }

    private void applyFilters() {
        if (filteredMachines != null) {
            String selectedStatus = statusFilterComboBox.getValue();
            String searchText = searchBar.getText().toLowerCase();

            filteredMachines.setPredicate(machine -> {
                boolean matchesStatus = selectedStatus == null || selectedStatus.equals("All Statuses")
                        || (machine.getCurrentStateString() != null && machine.getCurrentStateString().equalsIgnoreCase(selectedStatus));

                boolean matchesSearch = searchText == null || searchText.isEmpty()
                        || (machine.getCode() != null && machine.getCode().toLowerCase().contains(searchText))
                        || (machine.getTechnieker() != null && machine.getTechnieker().getFirstName().toLowerCase().contains(searchText))
                        || (machine.getTechnieker() != null && machine.getTechnieker().getLastName().toLowerCase().contains(searchText));

                return matchesStatus && matchesSearch;
            });

            updateMachineCards();
        }
    }

    private void loadMachineCards() {
        if (machines != null) {
            machines.clear();
        }

        if (currentSite != null && currentSite.getMachines() != null) {
            machines = FXCollections.observableArrayList(currentSite.getMachines());
            filteredMachines = new FilteredList<>(machines, p -> true);
            updateMachineCards();
        }
    }

    private void updateMachineCards() {
        machineCardsContainer.getChildren().clear();

        for (Machine machine : filteredMachines) {
            VBox card = createMachineCard(machine);
            machineCardsContainer.getChildren().add(card);
        }
    }

    private VBox createMachineCard(Machine machine) {
        VBox card = new VBox();
        card.getStyleClass().add("machine-card");
        card.setSpacing(10);
        card.setPadding(new Insets(15));

        Label codeLabel = new Label(machine.getCode());
        codeLabel.getStyleClass().add("machine-code");

        Label siteLabel = new Label("Site: " + currentSite.getName());
        siteLabel.getStyleClass().add("machine-site");

        String technicianName = machine.getTechnieker() != null ?
                machine.getTechnieker().getFirstName() + " " + machine.getTechnieker().getLastName() : "Not assigned";
        Label technicianLabel = new Label("Technician: " + technicianName);
        technicianLabel.getStyleClass().add("machine-technician");

        HBox statusBox = new HBox(10);
        statusBox.setAlignment(Pos.CENTER_LEFT);

        Circle statusIndicator = new Circle(8);
        statusIndicator.getStyleClass().add("status-indicator");

        String currentState = machine.getCurrentStateString();
        if (currentState != null) {
            statusIndicator.getStyleClass().add(currentState.toLowerCase());
        }

        Label statusLabel = new Label(currentState);
        statusLabel.getStyleClass().addAll("status-text", currentState.toLowerCase());

        statusBox.getChildren().addAll(statusIndicator, statusLabel);

        ComboBox<String> statusDropdown = new ComboBox<>();
        statusDropdown.getStyleClass().add("status-dropdown");
        statusDropdown.setPromptText("Change status");
        statusDropdown.setItems(FXCollections.observableArrayList("running", "startable", "stopped"));

        if (currentState != null && !currentState.isEmpty()) {
            statusDropdown.setValue(currentState);
        }

        statusDropdown.setOnAction(event -> {
            String newStatus = statusDropdown.getValue();
            if (newStatus != null && !newStatus.equals(currentState)) {
                updateMachineStatus(machine, newStatus);
            }
        });

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        card.getChildren().addAll(codeLabel, siteLabel, technicianLabel, statusBox, spacer, statusDropdown);

        return card;
    }

    private void updateMachineStatus(Machine machine, String newStatus) {
        switch (newStatus.toLowerCase()) {
            case "running":
                sc.setMachineInStartable(machine);
                machine.setCurrentStateString("running");
                break;
            case "startable":
                sc.setMachineInStartable(machine);
                break;
            case "stopped":
                sc.stopMachine(machine);
                break;
            default:
                System.out.println("Unknown status: " + newStatus);
                return;
        }

        sc.updateMachine(machine);

        sc.logUserAction("Machine Status Change",
                "Changed status of machine " + machine.getCode() + " to " + newStatus);

        updateMachineCards();
    }
}