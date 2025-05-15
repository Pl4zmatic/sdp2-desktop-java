package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import domein.machine.Machine;
import domein.site.Site;
import domein.user.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import javafx.util.StringConverter;
import lombok.Getter;
import lombok.Setter;
import service.MachineService;
import service.ServiceController;
import service.SiteService;
import service.UserService;
import utils.Rollen;

public class SiteFormController {

    @Getter
    @Setter
    private Site site;

    private ServiceController sc;

    @FXML
    private BorderPane rootLayout;

    @FXML
    private VBox contentVBox;

    @FXML
    private Label formTitle;

    @FXML
    private VBox siteIdContainer;
    @FXML
    private TextField siteName;
    @FXML
    private TextField street;
    @FXML
    private TextField houseNumber;
    @FXML
    private TextField postalCode;
    @FXML
    private TextField city;
    @FXML
    private ComboBox<User> verantwoordelijkeComboBox;

    @FXML
    private RadioButton active;
    @FXML
    private RadioButton inactive;
    @FXML
    private ToggleGroup status;

    @FXML
    private Button save;
    @FXML
    private Button cancel;

    private Runnable onSaveCallback;
    private EventHandler<ActionEvent> closeHandler;

    private List<String> errors = new ArrayList<>();

    private boolean isEditingFlag;
    private ObservableList<Machine> siteMachines = FXCollections.observableArrayList();
    private ObservableList<Machine> availableMachines = FXCollections.observableArrayList();
    private FilteredList<User> filteredResponsibles;

    @FXML
    private void initialize() {
        this.sc = ServiceController.getInstance();

        setupCallbacks();
        setupScrollPane();
        setupResponsibleComboBox();
        isEditingFlag = false;

        if (formTitle != null) {
            formTitle.setText("Site Form");
        }
    }

    private void setupResponsibleComboBox() {
        List<User> allResponsibles = sc.getAllActiveUsers().stream()
                .filter(user -> user.getRol() == Rollen.VERANTWOORDELIJKE)
                .collect(Collectors.toList());

        if (allResponsibles.isEmpty()) {
            verantwoordelijkeComboBox.setDisable(true);
            verantwoordelijkeComboBox.setPromptText("No responsible persons available");
            return;
        }

        ObservableList<User> responsibles = FXCollections.observableArrayList(allResponsibles);

        filteredResponsibles = new FilteredList<>(responsibles, p -> true);

        verantwoordelijkeComboBox.setItems(filteredResponsibles);

        verantwoordelijkeComboBox.getStyleClass().add("comboBox");
        verantwoordelijkeComboBox.getStyleClass().add("filter-combo");

        verantwoordelijkeComboBox.setCellFactory(new Callback<ListView<User>, ListCell<User>>() {
            @Override
            public ListCell<User> call(ListView<User> param) {
                return new ListCell<User>() {
                    @Override
                    protected void updateItem(User item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setText(null);
                        } else {
                            setText(item.getFirstName() + " " + item.getLastName());
                        }
                    }
                };
            }
        });

        verantwoordelijkeComboBox.setConverter(new StringConverter<User>() {
            @Override
            public String toString(User user) {
                return user == null ? "" : user.getFirstName() + " " + user.getLastName();
            }

            @Override
            public User fromString(String string) {
                if (string == null || string.isEmpty()) {
                    return null;
                }
                return responsibles.stream()
                        .filter(user -> (user.getFirstName() + " " + user.getLastName()).equalsIgnoreCase(string))
                        .findFirst()
                        .orElse(null);
            }
        });

        verantwoordelijkeComboBox.setEditable(true);

        TextField editor = verantwoordelijkeComboBox.getEditor();

        final boolean[] isUpdatingFilter = new boolean[1];

        editor.textProperty().addListener((observable, oldValue, newValue) -> {
            if (isUpdatingFilter[0]) {
                return;
            }

            isUpdatingFilter[0] = true;
            try {
                filteredResponsibles.setPredicate(user -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }

                    String lowerCaseFilter = newValue.toLowerCase();
                    String fullName = user.getFirstName() + " " + user.getLastName();
                    return fullName.toLowerCase().contains(lowerCaseFilter);
                });

                if (filteredResponsibles.size() > 0 && !newValue.isEmpty()) {
                    if (!verantwoordelijkeComboBox.isShowing()) {
                        verantwoordelijkeComboBox.show();
                    }
                } else if (verantwoordelijkeComboBox.isShowing() && filteredResponsibles.isEmpty()) {
                    verantwoordelijkeComboBox.hide();
                }
            } finally {
                isUpdatingFilter[0] = false;
            }
        });

        verantwoordelijkeComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (isUpdatingFilter[0] || newVal == null) {
                return;
            }

            isUpdatingFilter[0] = true;
            try {
                editor.setText(newVal.getFirstName() + " " + newVal.getLastName());
                editor.positionCaret(editor.getText().length());
                editor.setStyle("-fx-text-fill: -deepBlue; -fx-font-weight: normal;");
            } finally {
                isUpdatingFilter[0] = false;
            }
        });

        editor.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.DOWN ||
                    event.getCode() == KeyCode.UP ||
                    event.getCode() == KeyCode.ENTER) {
                return;
            }
        });

        verantwoordelijkeComboBox.setPromptText("Select or type to search");
    }

    public void setupSaveOption() {
        if(site != null) {
            save.setText("Update");
            isEditingFlag = true;
            formTitle.setText("Edit Site");
        }
        else {
            save.setText("Add");
            isEditingFlag = false;
            formTitle.setText("Add Site");
        }
    }

    private void setupScrollPane() {
        if (rootLayout != null) {
            rootLayout.getStyleClass().add("edge-to-edge");
        }
    }

    private void setupCallbacks() {
        siteName.focusedProperty().addListener((event) -> checkTextField(siteName, "Site naam is vereist."));
        street.focusedProperty().addListener((event) -> checkTextField(street, "Straat is vereist."));
        houseNumber.focusedProperty().addListener((event) -> checkTextField(houseNumber, "Huisnummer is vereist."));
        postalCode.focusedProperty().addListener((event) -> checkTextField(postalCode, "Postcode is vereist."));
        city.focusedProperty().addListener((event) -> checkTextField(city, "Gemeente is vereist."));

        verantwoordelijkeComboBox.focusedProperty().addListener((event) -> checkResponsibleComboBox("Verantwoordelijke persoon is vereist."));

        save.setOnAction((event) -> saveSite());
    }

    private void checkResponsibleComboBox(String error) {
        errors.remove(error);
        if (verantwoordelijkeComboBox.getValue() == null && !verantwoordelijkeComboBox.isFocused()) {
            verantwoordelijkeComboBox.setPromptText(error);
            verantwoordelijkeComboBox.setStyle("-fx-prompt-text-fill: -bgRed;");
            errors.add(error);
        }
    }

    private void cancelCallback() {
        if (closeHandler != null) {
            closeHandler.handle(new ActionEvent());
        }

        if (onSaveCallback != null) {
            onSaveCallback.run();
        }
    }

    private <T extends TextInputControl> void checkTextField(T source, String error) {
        errors.remove(error);
        if ((source.getText().isBlank() || source.getText().isEmpty()) && !source.isFocused()) {
            source.setPromptText(error);
            source.setStyle("-fx-prompt-text-fill: -bgRed;");
            errors.add(error);
        }
    }

    private void checkToggleGroup(ToggleGroup t, String error) {
        errors.remove(error);
        if (t.getSelectedToggle() == null) {
            errors.add(error);
        }
    }

    private void checkSiteName(String error) {
        errors.remove(error);
        if (!isEditingFlag) {
            List<String> siteNamesInDatabase = sc.getAllSites().stream()
                    .map((site) -> site.getName())
                    .collect(Collectors.toList());
            if (siteNamesInDatabase.contains(siteName.getText().trim())) {
                errors.add(error);
            }
        }
    }

    private void checkAll() {
        if(!isEditingFlag) {
            checkSiteName("Site naam moet uniek zijn.");
        }

        checkTextField(siteName, "Site naam is vereist.");
        checkTextField(street, "Straat is vereist.");
        checkTextField(houseNumber, "Huisnummer is vereist.");
        checkTextField(postalCode, "Postcode is vereist.");
        checkTextField(city, "Gemeente is vereist.");
        checkResponsibleComboBox("Verantwoordelijke persoon is vereist.");
        checkToggleGroup(status, "Status is vereist.");
    }

    private void saveSite() {
        checkAll();
        if (!errors.isEmpty()) {
            // errors tonen
            String errorNotification = "";
            for (String err : errors) {
                errorNotification += err + "\n";
            }
            new Alert(AlertType.ERROR, errorNotification, ButtonType.OK).showAndWait();
        } else {
            if (site == null) {
                site = new Site();
            }

            site.setName(siteName.getText());

            String fullAddress = street.getText() + " " + houseNumber.getText() + ", " +
                    postalCode.getText() + " " + city.getText();
            site.setAddress(fullAddress);

            User selectedResponsible = verantwoordelijkeComboBox.getValue();
            if (selectedResponsible != null) {
                site.setVerantwoordelijke(selectedResponsible);
            }

            if (site.getMachines() == null) {
                site.setMachines(new HashSet<>(siteMachines));
            } else {
                site.getMachines().clear();
                site.getMachines().addAll(siteMachines);
            }

            boolean isActive = ((RadioButton) status.getSelectedToggle()).equals(active);
            site.setDeleted(!isActive);

            boolean success;
            if (isEditingFlag) {
                success = sc.updateSite(site);
            } else {
                success = sc.addSite(site);
            }

            if (success) {
                String message = isEditingFlag ? "Site successfully updated!" : "Site successfully created!";
                new Alert(AlertType.INFORMATION, message, ButtonType.OK).showAndWait();

                if (onSaveCallback != null) {
                    onSaveCallback.run();
                }
            } else {
                new Alert(AlertType.ERROR, "Failed to save site.", ButtonType.OK).showAndWait();
            }
        }
    }

    public void fillFieldData() {
        if (site != null) {
            siteName.setText(site.getName());

            String address = site.getAddress();
            if (address != null && !address.isEmpty()) {
                String[] parts = address.split(",");
                String streetPart = parts[0].trim();
                String cityPart = parts.length > 1 ? parts[1].trim() : "";

                int lastSpaceIndex = streetPart.lastIndexOf(" ");
                if (lastSpaceIndex > 0) {
                    street.setText(streetPart.substring(0, lastSpaceIndex));
                    houseNumber.setText(streetPart.substring(lastSpaceIndex + 1));
                } else {
                    street.setText(streetPart);
                    houseNumber.setText("");
                }

                String[] cityParts = cityPart.split(" ", 2);
                postalCode.setText(cityParts.length > 0 ? cityParts[0] : "");
                city.setText(cityParts.length > 1 ? cityParts[1] : "");
            }

            String responsibleName = site.getVerantwoordelijke().getFullName();
            if (responsibleName != null && !responsibleName.isEmpty()) {
                for (User responsible : filteredResponsibles) {
                    String fullName = responsible.getFirstName() + " " + responsible.getLastName();
                    if (fullName.equals(responsibleName)) {
                        verantwoordelijkeComboBox.setValue(responsible);
                        break;
                    }
                }

                if (verantwoordelijkeComboBox.getValue() == null) {
                    verantwoordelijkeComboBox.getEditor().setText(responsibleName);
                }
            }

            siteMachines.clear();
            if (site.getMachines() != null) {
                siteMachines.addAll(site.getMachines());
            }

            boolean isDeleted = site.getDeleted();
            if (isDeleted) {
                inactive.setSelected(true);
            } else {
                active.setSelected(true);
            }
        } else {
            siteName.setText("");
            street.setText("");
            houseNumber.setText("");
            postalCode.setText("");
            city.setText("");
            verantwoordelijkeComboBox.setValue(null);
            siteMachines.clear();

            active.setSelected(true);
        }
    }

    public void addCloseButton(EventHandler<ActionEvent> closeHandler) {
        this.closeHandler = closeHandler;

        if (cancel != null) {
            cancel.setOnAction(event -> cancelCallback());
        }
    }

    public void setOnSaveCallback(Runnable callback) {
        this.onSaveCallback = callback;
    }
}

