package controller;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import domein.machine.Machine;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;
import service.MachineService;

public class ManageMachinesController {
        @FXML
        private BorderPane rootLayout;

        @FXML
        private TextField searchBar;
        @FXML
        private HBox searchBarContainer;

        @FXML
        private ComboBox<String> locationFilterComboBox;

        @FXML
        private Button addMachineButton;

        @FXML
        private CheckBox showDeletedMachines;

        @FXML
        private TableView<Machine> tableView;

        @FXML
        private StackPane rightPanelContainer;

        private ObservableList<Machine> machines;
        private FilteredList<Machine> filteredMachines;
        private MachineService machineService;
        private MachineFormController formController;

        @FXML
        private void initialize() throws IOException {
                machineService = MachineService.getInstance();
                setupTable();
                setupNavbar();
                setupLocationFilter();
                loadTableContent();
                setupSearchFilter();
        }

        private void setupNavbar() throws IOException {
                this.rootLayout.setLeft(NavbarManager.getNavbar());
        }

        private void setupLocationFilter() {
                List<String> locations = machineService.getAllLocations().stream()
                        .distinct()
                        .sorted()
                        .collect(Collectors.toList());

                ObservableList<String> locationOptions = FXCollections.observableArrayList();
                locationOptions.add("All Locations"); // Optie om filter te resetten
                locationOptions.addAll(locations);

                locationFilterComboBox.setItems(locationOptions);
                locationFilterComboBox.getSelectionModel().selectFirst(); // "All Locations" standaard geselecteerd

                locationFilterComboBox.setOnAction(event -> applyFilters());
        }

        private void setupSearchFilter() {
                searchBar.textProperty().addListener((observable, oldValue, newValue) -> applyFilters());
        }

        private void applyFilters() {
                if (filteredMachines != null) {
                        String selectedLocation = locationFilterComboBox.getValue();
                        String searchText = searchBar.getText().toLowerCase();

                        filteredMachines.setPredicate(machine -> {
                                String laatsteOnderhoudDatum = (machine.getLaatsteOnderhoudDatum() != null)
                                        ? machine.getLaatsteOnderhoudDatum().toString()
                                        : "";
                                String nextOnderhoudDatum = (machine.getDatumToekomstigeOnderhoud() != null)
                                        ? machine.getLaatsteOnderhoudDatum().toString()
                                        : "";
                                boolean matchesLocation = selectedLocation == null || selectedLocation.equals("All Locations")
                                        || machine.getLocatie().equals(selectedLocation);

                                boolean matchesSearch = searchText == null || searchText.isEmpty()
                                        || machine.getSiteNaam().toLowerCase().contains(searchText)
                                        || machine.getCode().toLowerCase().contains(searchText)
                                        || machine.getLocatie().toLowerCase().contains(searchText)
                                        || machine.getCurrentState().toLowerCase().contains(searchText)
                                        || laatsteOnderhoudDatum.toLowerCase().contains(searchText)
                                        || nextOnderhoudDatum.toLowerCase().contains(searchText);

                                return matchesLocation && matchesSearch;
                        });
                }
        }
        private void setupTable() {
                tableView.getColumns().clear();

                // Maak kolommen aan
                TableColumn<Machine, String> siteColumn = new TableColumn<>("Site");
                TableColumn<Machine, String> codeColumn = new TableColumn<>("Code");
                TableColumn<Machine, String> statusColumn = new TableColumn<>("Status");
                TableColumn<Machine, String> lastMaintenanceColumn = new TableColumn<>("Last Maintenance");
                TableColumn<Machine, String> nextMaintenanceColumn = new TableColumn<>("Next Maintenance");
                TableColumn<Machine, Void> actionsColumn = new TableColumn<>("Actions");

                // Stel de cell value factories in
                siteColumn.setCellValueFactory(new PropertyValueFactory<>("siteNaam"));
                codeColumn.setCellValueFactory(new PropertyValueFactory<>("code"));
                statusColumn.setCellValueFactory(new PropertyValueFactory<>("currentStateString"));
                lastMaintenanceColumn.setCellValueFactory(new PropertyValueFactory<>("laatsteOnderhoudDatum"));
                nextMaintenanceColumn.setCellValueFactory(new PropertyValueFactory<>("datumToekomstigeOnderhoud"));

                // Stel de breedtes in
                siteColumn.setPrefWidth(150);
                codeColumn.setPrefWidth(120);
                statusColumn.setPrefWidth(150);
                lastMaintenanceColumn.setPrefWidth(180);
                nextMaintenanceColumn.setPrefWidth(180);
                actionsColumn.setPrefWidth(200);
                actionsColumn.setMinWidth(160);

                siteColumn.setStyle("-fx-alignment: CENTER-LEFT;");
                codeColumn.setStyle("-fx-alignment: CENTER-LEFT;");
                statusColumn.setStyle("-fx-alignment: CENTER-LEFT;");
                lastMaintenanceColumn.setStyle("-fx-alignment: CENTER-LEFT;");
                nextMaintenanceColumn.setStyle("-fx-alignment: CENTER-LEFT;");
                actionsColumn.setStyle("-fx-alignment: CENTER;");

                // Maak de actiekolom met knoppen
                actionsColumn.setCellFactory(new Callback<TableColumn<Machine, Void>, TableCell<Machine, Void>>() {
                        @Override
                        public TableCell<Machine, Void> call(final TableColumn<Machine, Void> param) {
                                return new TableCell<Machine, Void>() {
                                        private final HBox hbox = new HBox(10);
                                        private final Button editButton = new Button("Edit");
                                        private final Button deleteButton = new Button("Delete");

                                        {
                                                hbox.setAlignment(Pos.CENTER);
                                                hbox.getStyleClass().add("actions-container");
                                                hbox.setMinHeight(40);
                                                hbox.setPrefHeight(40);

                                                editButton.getStyleClass().add("edit-button");
                                                editButton.setMaxWidth(Double.MAX_VALUE);
                                                editButton.setMinHeight(30);
                                                editButton.setPrefHeight(30);
                                                HBox.setHgrow(editButton, Priority.ALWAYS);

                                                deleteButton.getStyleClass().add("delete-button");
                                                deleteButton.setMaxWidth(Double.MAX_VALUE);
                                                deleteButton.setMinHeight(30);
                                                deleteButton.setPrefHeight(30);
                                                HBox.setHgrow(deleteButton, Priority.ALWAYS);

                                                hbox.getChildren().addAll(editButton, deleteButton);
                                        }

                                        @Override
                                        protected void updateItem(Void item, boolean empty) {
                                                super.updateItem(item, empty);
                                                if (empty) {
                                                        setGraphic(null);
                                                } else {
                                                        setGraphic(hbox);

                                                        // Zorg ervoor dat de knoppen de juiste actie uitvoeren
                                                        Machine machine = getTableView().getItems().get(getIndex());

                                                        editButton.setOnAction(event -> {
                                                                editMachine(machine);
                                                        });

                                                        deleteButton.setOnAction(event -> {
                                                                deleteMachine(machine);
                                                        });
                                                }
                                        }
                                };
                        }
                });

                // Voeg alle kolommen toe aan de tabel
                tableView.getColumns().addAll(
                        siteColumn, codeColumn, statusColumn,
                        lastMaintenanceColumn, nextMaintenanceColumn, actionsColumn
                );

                // Stel de tabel in om niet te groeien buiten de kolommen
                tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

                // Bereken de totale breedte van alle kolommen
                double totalWidth = siteColumn.getPrefWidth() + codeColumn.getPrefWidth() +
                        statusColumn.getPrefWidth() + lastMaintenanceColumn.getPrefWidth() +
                        nextMaintenanceColumn.getPrefWidth() + actionsColumn.getPrefWidth();

                // Stel de breedte van de tabel in
                tableView.setPrefWidth(totalWidth + 50);


                // Zorg ervoor dat de rijen voldoende hoogte hebben
                tableView.setFixedCellSize(50);
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
                                        hideRightPanel(); // Verberg het rechterpaneel na verwijderen
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
                        Parent formRoot = loader.load();

                        // Haal de controller op en configureer deze
                        formController = loader.getController();
                        formController.setMachine(machine);
                        formController.setupSaveOption();
                        formController.fillFieldData();

                        // Voeg een knop toe om het formulier te sluiten
                        formController.addCloseButton(event -> hideRightPanel());

                        // Voeg een callback toe voor na het opslaan
                        formController.setOnSaveCallback(() -> {
                                refreshTable();
                                // Optioneel: sluit het formulier na opslaan
                                // hideRightPanel();
                        });

                        // Toon het formulier in het rechterpaneel
                        showRightPanel(formRoot);
                } catch (IOException e) {
                        e.printStackTrace();
                }
        }

        private void showRightPanel(Parent content) {
                rightPanelContainer.getChildren().clear();
                rightPanelContainer.getChildren().add(content);
                rightPanelContainer.setVisible(true);
                rightPanelContainer.setManaged(true);
        }

        private void hideRightPanel() {
                rightPanelContainer.getChildren().clear();
                rightPanelContainer.setVisible(false);
                rightPanelContainer.setManaged(false);
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
                                Machine selectedMachine = tableView.getSelectionModel().getSelectedItem();
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
                        Parent formRoot = loader.load();

                        // Haal de controller op en configureer deze
                        formController = loader.getController();
                        formController.setMachine(null); // Nieuwe machine
                        formController.setupSaveOption();

                        // Voeg een knop toe om het formulier te sluiten
                        formController.addCloseButton(event -> hideRightPanel());

                        // Voeg een callback toe voor na het opslaan
                        formController.setOnSaveCallback(() -> {
                                refreshTable();
                                // Optioneel: sluit het formulier na opslaan
                                // hideRightPanel();
                        });

                        // Toon het formulier in het rechterpaneel
                        showRightPanel(formRoot);
                } catch (IOException e) {
                        e.printStackTrace();
                }
        }

        protected void selectMachine(Machine m) {
                tableView.getSelectionModel().clearSelection();
                tableView.getSelectionModel().select(m);
        }

        public void refreshTable() {
                loadTableContent();
        }
}