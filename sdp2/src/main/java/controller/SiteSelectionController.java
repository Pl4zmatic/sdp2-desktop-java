package controller;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import domein.Session;
import domein.site.Site;
import domein.user.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import service.ServiceController;
import service.SiteService;
import utils.Rollen;

public class SiteSelectionController {
    @FXML
    private BorderPane rootLayout;

    @FXML
    private TextField searchBar;

    @FXML
    private ComboBox<String> locationFilterComboBox;

    @FXML
    private FlowPane plantCardsContainer;

    private ObservableList<Site> plants;
    private FilteredList<Site> filteredPlants;
    private ServiceController sc;

    @FXML
    private void initialize() throws IOException {
        sc = ServiceController.getInstance();
        setupNavbar();
        setupLocationFilter();
        loadPlantCards();
        setupSearchFilter();
    }

    private void setupNavbar() throws IOException {
        this.rootLayout.setLeft(NavbarManager.getNavbar());
    }

    private void setupLocationFilter() {
        List<String> cities = sc.getAllSites().stream()
                .filter(site -> !site.getDeleted())
                .filter(this::userHasAccessToSite)
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
        if (filteredPlants != null) {
            String selectedLocation = locationFilterComboBox.getValue();
            String searchText = searchBar.getText().toLowerCase();

            filteredPlants.setPredicate(site -> {
                boolean matchesLocation = selectedLocation == null || selectedLocation.equals("All Locations")
                        || (site.getAddress() != null && site.getAddress().toLowerCase().contains(selectedLocation.toLowerCase()));

                boolean matchesSearch = searchText == null || searchText.isEmpty()
                        || (site.getName() != null && site.getName().toLowerCase().contains(searchText))
                        || (site.getAddress() != null && site.getAddress().toLowerCase().contains(searchText))
                        || (site.getVerantwoordelijke() != null && site.getVerantwoordelijke().toLowerCase().contains(searchText));

                boolean isActive = !site.getDeleted();

                boolean hasAccess = userHasAccessToSite(site);

                return matchesLocation && matchesSearch && isActive && hasAccess;
            });

            updatePlantCards();
        }
    }

    private void loadPlantCards() {
        if (plants != null) {
            plants.clear();
        }

        List<Site> allSites = sc.getAllSites();

        List<Site> accessibleSites = allSites.stream()
                .filter(this::userHasAccessToSite)
                .collect(Collectors.toList());

        plants = FXCollections.observableArrayList(accessibleSites);
        filteredPlants = new FilteredList<>(plants, site -> !site.getDeleted());

        updatePlantCards();
    }

    private boolean userHasAccessToSite(Site site) {
        User currentUser = Session.getCurrentUser();
        if (currentUser == null) return false;

        if (currentUser.getRol() == Rollen.MANAGER ||
                currentUser.getRol() == Rollen.ADMINISTRATOR) {
            return true;
        }

        if (currentUser.getRol() == Rollen.VERANTWOORDELIJKE) {
            String fullName = currentUser.getFirstName() + " " + currentUser.getLastName();
            return site.getVerantwoordelijke() != null &&
                    site.getVerantwoordelijke().equals(fullName);
        }

        return false;
    }

    private void updatePlantCards() {
        plantCardsContainer.getChildren().clear();

        for (Site plant : filteredPlants) {
            VBox card = createPlantCard(plant);
            plantCardsContainer.getChildren().add(card);
        }
    }

    private VBox createPlantCard(Site plant) {
        VBox card = new VBox();
        card.getStyleClass().add("plant-card");
        card.setPrefWidth(280);
        card.setPrefHeight(200);
        card.setPadding(new Insets(15));
        card.setSpacing(10);

        Label nameLabel = new Label(plant.getName());
        nameLabel.getStyleClass().add("plant-name");
        nameLabel.setFont(Font.font("Inter", FontWeight.BOLD, 18));
        nameLabel.setWrapText(true);

        Label addressLabel = new Label(plant.getAddress());
        addressLabel.getStyleClass().add("plant-address");
        addressLabel.setWrapText(true);

        HBox responsibleBox = new HBox(5);
        responsibleBox.setAlignment(Pos.CENTER_LEFT);
        Label responsibleLabel = new Label("Responsible: ");
        responsibleLabel.getStyleClass().add("plant-info-label");
        Label responsibleValue = new Label(plant.getVerantwoordelijke());
        responsibleValue.getStyleClass().add("plant-info-value");
        responsibleBox.getChildren().addAll(responsibleLabel, responsibleValue);

        HBox machineBox = new HBox(5);
        machineBox.setAlignment(Pos.CENTER_LEFT);
        Label machineLabel = new Label("Machines: ");
        machineLabel.getStyleClass().add("plant-info-label");
        int machineCount = plant.getMachines() != null ? plant.getMachines().size() : 0;
        Label machineValue = new Label(String.valueOf(machineCount));
        machineValue.getStyleClass().add("plant-info-value");
        machineBox.getChildren().addAll(machineLabel, machineValue);

        Button selectButton = new Button("Select Plant");
        selectButton.getStyleClass().add("select-plant-button");
        selectButton.setPrefWidth(Double.MAX_VALUE);
        selectButton.setOnAction(event -> selectPlant(plant));

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        card.getChildren().addAll(nameLabel, addressLabel, responsibleBox, machineBox, spacer, selectButton);

        return card;
    }

    protected void selectPlant(Site plant) {
        Session.setCurrentSite(plant);
        SceneSwitcher.switchScene("/view/ManageMachines.fxml");
    }
}

