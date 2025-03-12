package controller;
import domein.logging.LogEntry;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import service.LogService;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class LogController implements Initializable {

    @FXML
    private MFXTableView<LogEntry> logTable;

    @FXML
    private ComboBox<String> actionFilterComboBox;

    @FXML
    private DatePicker dateFilterPicker;

    @FXML
    private TextField searchField;
    @FXML
    private BorderPane rootLayout;

    private final LogService logService = LogService.getInstance();
    private ObservableList<LogEntry> logEntries;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    // Lijst van acties die we willen tonen
    private final List<String> ALLOWED_ACTIONS = Arrays.asList(
            "USER_ADD",
            "USER_EDIT",
            "USER_DELETE",
            "PASSWORD_RESET",
            "MACHINE_EDIT"
    );

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        rootLayout.setLeft(NavbarManager.getNavbar());

        setupTable();
        setupFilters();
        loadData();
        logTable.setFooterVisible(false);
    }

    private void setupTable() {
        // Timestamp kolom
        MFXTableColumn<LogEntry> timestampColumn = new MFXTableColumn<>("Tijdstip", true);
        timestampColumn.setMinWidth(150);
        timestampColumn.getStyleClass().add("timestamp-column");
        timestampColumn.setComparator(Comparator.comparing(LogEntry::getTimestamp));
        timestampColumn.setRowCellFactory(entry -> {
            MFXTableRowCell<LogEntry, LocalDateTime> cell = new MFXTableRowCell<>(LogEntry::getTimestamp,
                    datetime -> datetime.format(formatter));
            return cell;
        });

        // Action kolom
        MFXTableColumn<LogEntry> actionColumn = new MFXTableColumn<>("Actie", true);
        actionColumn.setMinWidth(150);
        actionColumn.getStyleClass().add("action-column");
        actionColumn.setComparator(Comparator.comparing(LogEntry::getAction));
        actionColumn.setRowCellFactory(entry -> {
            MFXTableRowCell<LogEntry, String> cell = new MFXTableRowCell<>(LogEntry::getAction, action -> {
                // Format de types naar deftige tekst
                switch (action) {
                    case "GEBRUIKER_AANGEMAAKT": return "Gebruiker Aangemaakt";
                    case "GEBRUIKER_GEWIJZIGD": return "Gebruiker Gewijzigd";
                    case "GEBRUIKER_VERWIJDERD": return "Gebruiker Verwijderd";
                    case "WACHTWOORD_RESET": return "Wachtwoord Reset";
                    default: return action;
                }
            });

            // Voeg CSS classes toe op basis van actie type
            String action = entry.getAction();
            if ("USER_ADD".equals(action)) {
                cell.getStyleClass().add("user-created");
            } else if ("USER_EDIT".equals(action)) {
                cell.getStyleClass().add("user-edited");
            } else if ("USER_DELETE".equals(action)) {
                cell.getStyleClass().add("user-deleted");
            } else if ("PASSWORD_RESET".equals(action)) {
                cell.getStyleClass().add("password-reset");
            }

            return cell;
        });

        // Details kolom
        MFXTableColumn<LogEntry> detailsColumn = new MFXTableColumn<>("Details", true);
        detailsColumn.setMinWidth(400);
        detailsColumn.getStyleClass().add("details-column");
        detailsColumn.setComparator(Comparator.comparing(LogEntry::getDetails));
        detailsColumn.setRowCellFactory(entry -> new MFXTableRowCell<>(LogEntry::getDetails));

        // Voeg kolommen toe aan tabel
        logTable.getTableColumns().addAll(timestampColumn, actionColumn, detailsColumn);

        // Configureer tabel
        logTable.setFooterVisible(true);
        logTable.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
    }

    private void setupFilters() {
        // Actie filter
        List<String> actionTypes = Arrays.asList(
                "All Actions",
                "User Added",
                "User Editted",
                "User Deleted",
                "Password Reset"
        );
        actionFilterComboBox.setItems(FXCollections.observableArrayList(actionTypes));
        actionFilterComboBox.getSelectionModel().selectFirst();

        actionFilterComboBox.setOnAction(event -> filterLogs());

        // Datum filter
        dateFilterPicker.setOnAction(event -> filterLogs());

        // Zoek filter
        searchField.textProperty().addListener((observable, oldValue, newValue) -> filterLogs());
    }

    private void loadData() {
        // Haal alleen de toegestane acties op
        List<LogEntry> logs = logService.getAllLogs().stream()
                .filter(log -> ALLOWED_ACTIONS.contains(log.getAction()))
                .sorted(Comparator.comparing(LogEntry::getTimestamp).reversed())
                .collect(Collectors.toList());

        logEntries = FXCollections.observableArrayList(logs);
        logTable.setItems(logEntries);
    }

    private void filterLogs() {
        String actionFilter = actionFilterComboBox.getValue();
        LocalDate dateFilter = dateFilterPicker.getValue();
        String searchText = searchField.getText().toLowerCase();

        List<LogEntry> filteredLogs = logService.getAllLogs().stream()
                .filter(log -> ALLOWED_ACTIONS.contains(log.getAction()))
                .filter(log -> {
                    // Filter op actie
                    if (actionFilter != null && !"All Actions".equals(actionFilter)) {
                        String actionCode = "";
                        // Converteer gebruiksvriendelijke tekst terug naar actie code
                        if ("User Added".equals(actionFilter)) actionCode = "USER_ADD";
                        else if ("User Editted".equals(actionFilter)) actionCode = "USER_EDIT";
                        else if ("User Deleted".equals(actionFilter)) actionCode = "USER_DELETE";
                        else if ("Password Reset".equals(actionFilter)) actionCode = "PASSWORD_RESET";

                        if (!log.getAction().equals(actionCode)) {
                            return false;
                        }
                    }

                    // Filter op datum
                    if (dateFilter != null) {
                        LocalDate logDate = log.getTimestamp().toLocalDate();
                        if (!logDate.equals(dateFilter)) {
                            return false;
                        }
                    }

                    // Filter op zoektekst
                    if (searchText != null && !searchText.isEmpty()) {
                        return log.getAction().toLowerCase().contains(searchText) ||
                                log.getDetails().toLowerCase().contains(searchText);
                    }

                    return true;
                })
                .collect(Collectors.toList());

        logEntries.clear();
        logEntries.addAll(filteredLogs);
    }
}