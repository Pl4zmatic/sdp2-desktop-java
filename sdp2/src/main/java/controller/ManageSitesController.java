package controller;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import domein.site.Site;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;
import service.ServiceController;
import service.SiteService;

public class ManageSitesController {
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
    private TableView<Site> tableView;

    @FXML
    private StackPane rightPanelContainer;

    private ObservableList<Site> sites;
    private FilteredList<Site> filteredSites;
    private ServiceController sc;
    private SiteFormController formController;

    @FXML
    private void initialize() throws IOException {
        sc = ServiceController.getInstance();
        setupTable();
        setupNavbar();
        setupLocationFilter();
        loadTableContent();
        setupSearchFilter();
        setupCallbacks();
    }

    private void setupNavbar() throws IOException {
        this.rootLayout.setLeft(NavbarManager.getNavbar());
    }

    private void setupLocationFilter() {
        List<String> cities = sc.getAllSites().stream()
                .map(site -> {
                    String address = site.getAddress();
                    if (address != null && address.contains(",")) {
                        String cityPart = address.split(",")[1].trim();
                        if (cityPart.contains(" ")) {
                            return cityPart.split(" ", 2)[1];
                        }
                        return cityPart;
                    }
                    return "";
                })
                .filter(city -> !city.isEmpty())
                .distinct()
                .sorted()
                .collect(Collectors.toList());

        ObservableList<String> locationOptions = FXCollections.observableArrayList();
        locationOptions.add("All Locations");
        locationOptions.addAll(cities);

        locationFilterComboBox.setItems(locationOptions);
        locationFilterComboBox.getSelectionModel().selectFirst();

        locationFilterComboBox.setOnAction(event -> applyFilters());
    }

    private void setupSearchFilter() {
        searchBar.textProperty().addListener((observable, oldValue, newValue) -> applyFilters());
    }

    private void applyFilters() {
        if (filteredSites != null) {
            String selectedLocation = locationFilterComboBox.getValue();
            String searchText = searchBar.getText().toLowerCase();

            filteredSites.setPredicate(site -> {
                boolean matchesLocation = selectedLocation == null || selectedLocation.equals("All Locations")
                        || (site.getAddress() != null && site.getAddress().toLowerCase().contains(selectedLocation.toLowerCase()));

                boolean matchesSearch = searchText == null || searchText.isEmpty()
                        || (site.getName() != null && site.getName().toLowerCase().contains(searchText))
                        || (site.getAddress() != null && site.getAddress().toLowerCase().contains(searchText))
                        || (site.getVerantwoordelijke() != null && site.getVerantwoordelijke().getFullName().toLowerCase().contains(searchText));

                return matchesLocation && matchesSearch;
            });
        }
    }

    private void setupTable() {
        tableView.getColumns().clear();

        TableColumn<Site, String> idColumn = new TableColumn<>("ID");
        TableColumn<Site, String> nameColumn = new TableColumn<>("Name");
        TableColumn<Site, String> addressColumn = new TableColumn<>("Address");
        TableColumn<Site, String> responsibleColumn = new TableColumn<>("Responsible Person");
        TableColumn<Site, Integer> machinesColumn = new TableColumn<>("Machines Count");
        TableColumn<Site, Void> actionsColumn = new TableColumn<>("Actions");

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));

        responsibleColumn.setCellValueFactory(new Callback<CellDataFeatures<Site, String>, ObservableValue<String>>() {
            public javafx.beans.value.ObservableValue<String> call(CellDataFeatures<Site,String> param) {
                return new ReadOnlyObjectWrapper<>(param.getValue().getVerantwoordelijke().getFullName());
            };
        });

        machinesColumn.setCellValueFactory(cellData -> {
            Site site = cellData.getValue();
            int count = site.getMachines() != null ? site.getMachines().size() : 0;
            return new javafx.beans.property.SimpleIntegerProperty(count).asObject();
        });

        idColumn.setPrefWidth(100);
        nameColumn.setPrefWidth(150);
        addressColumn.setPrefWidth(200);
        responsibleColumn.setPrefWidth(150);
        machinesColumn.setPrefWidth(120);
        actionsColumn.setPrefWidth(200);
        actionsColumn.setMinWidth(160);

        idColumn.setStyle("-fx-alignment: CENTER-LEFT;");
        nameColumn.setStyle("-fx-alignment: CENTER-LEFT;");
        addressColumn.setStyle("-fx-alignment: CENTER-LEFT;");
        responsibleColumn.setStyle("-fx-alignment: CENTER-LEFT;");
        machinesColumn.setStyle("-fx-alignment: CENTER;");
        actionsColumn.setStyle("-fx-alignment: CENTER;");

        actionsColumn.setCellFactory(new Callback<TableColumn<Site, Void>, TableCell<Site, Void>>() {
            @Override
            public TableCell<Site, Void> call(final TableColumn<Site, Void> param) {
                return new TableCell<Site, Void>() {
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

                            Site site = getTableView().getItems().get(getIndex());

                            editButton.setOnAction(event -> {
                                editSite(site);
                            });

                            deleteButton.setOnAction(event -> {
                                deleteSite(site);
                            });
                        }
                    }
                };
            }
        });

        tableView.getColumns().addAll(
                idColumn, nameColumn, addressColumn,
                responsibleColumn, machinesColumn, actionsColumn
        );

        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        double totalWidth = idColumn.getPrefWidth() + nameColumn.getPrefWidth() +
                addressColumn.getPrefWidth() + responsibleColumn.getPrefWidth() +
                machinesColumn.getPrefWidth() + actionsColumn.getPrefWidth();

        tableView.setPrefWidth(totalWidth + 50);

        tableView.setFixedCellSize(50);
    }

    private Site findSiteById(int id) {
        for (Site site : sites) {
            if (site.getId() == id) {
                return site;
            }
        }
        return null;
    }

    private void deleteSite(Site site) {
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure you want to delete site " + site.getName() + "?",
                ButtonType.YES, ButtonType.NO);
        confirmDialog.setTitle("Confirm Delete");
        confirmDialog.setHeaderText("Delete Site");

        confirmDialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                boolean success = sc.deleteSite(site.getId());
                if (success) {
                    refreshTable();
                    hideRightPanel();
                } else {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR,
                            "Failed to delete site with ID: " + site.getId(),
                            ButtonType.OK);
                    errorAlert.setTitle("Error");
                    errorAlert.setHeaderText("Delete Failed");
                    errorAlert.showAndWait();
                }
            }
        });
    }

    private void editSite(Site site) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/SiteForm.fxml"));
            Parent formRoot = loader.load();

            formController = loader.getController();
            formController.setSite(site);
            formController.setupSaveOption();
            formController.fillFieldData();

            formController.addCloseButton(event -> hideRightPanel());

            formController.setOnSaveCallback(() -> {
                refreshTable();
                hideRightPanel();
            });

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

        tableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Site selectedSite = tableView.getSelectionModel().getSelectedItem();
                if (selectedSite != null) {
                    editSite(selectedSite);
                }
            }
        });
    }

    private void loadTableContent() {
        if (sites != null) {
            sites.clear();
        }

        if (showDeletedMachines.isSelected()) {
            sites = FXCollections.observableArrayList(sc.getAllSites());
        } else {
            sites = FXCollections.observableArrayList(
                    sc.getAllSites().stream()
                            .filter(site -> !site.getDeleted())
                            .collect(Collectors.toList())
            );
        }

        filteredSites = new FilteredList<>(sites, p -> true);

        tableView.getItems().clear();
        tableView.setItems(filteredSites);
    }

    private void addButtonCallback() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/SiteForm.fxml"));
            Parent formRoot = loader.load();

            formController = loader.getController();
            formController.setupSaveOption();
            formController.fillFieldData();

            formController.addCloseButton(event -> hideRightPanel());

            formController.setOnSaveCallback(() -> {
                refreshTable();
                hideRightPanel();
            });

            showRightPanel(formRoot);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void selectSite(Site s) {
        tableView.getSelectionModel().clearSelection();
        tableView.getSelectionModel().select(s);
    }

    public void refreshTable() {
        loadTableContent();
    }
}