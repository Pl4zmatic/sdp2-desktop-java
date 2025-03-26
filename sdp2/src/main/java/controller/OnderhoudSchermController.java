package controller;

import java.awt.Component;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import domein.Session;
import domein.machine.Machine;
import domein.machine.Maintenance;
import domein.machine.report.Report;
import domein.machine.stateMachines.machine.StoppedState;
import domein.machine.stateMachines.maintenance.FinishedState;
import domein.machine.stateMachines.maintenance.MaintenanceState;
import domein.machine.stateMachines.maintenance.PlannedState;
import domein.machine.stateMachines.maintenance.ProgressState;
import domein.user.User;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.virtualizedfx.enums.ScrollPaneEnums.ScrollBarPolicy;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Callback;
import javafx.util.StringConverter;
import service.MachineService;
import service.MaintenanceService;
import service.ServiceController;
import utils.Rollen;

public class OnderhoudSchermController {


	private ServiceController sc;

	Maintenance selectedMaintenance;
	@FXML
	private MFXButton completedButton;

	@FXML
	private BorderPane rootLayout;
	@FXML
	private MFXButton cancelButton;
	
    @FXML
    private VBox fullProgressVBox;

	@FXML
	private VBox progressVBox;

	@FXML
	private VBox plannedVBox;

	@FXML
	private ComboBox<String> dateField;

	@FXML
	private ComboBox<Machine> machineField;

	@FXML
	private TextField notesField;

	@FXML
	private MFXButton planButton;

	@FXML
	private ScrollPane plannedScrollable;

	@FXML
	private ScrollPane progressScrollable;

	@FXML
	private TextField reasonField;

	@FXML
	private VBox fieldVBox;

	@FXML
	private MFXButton submitButton;

	@FXML
	private TextField techniekerField;

	@FXML
	private Button addReportButton;

	@FXML
	private Button editAndViewReportButton;

    private FilteredList<Machine> filteredMachines;
    private FilteredList<String> filteredDates;

	private ReportService reportService;
	private ReportPageController reportPageController;
    
	@FXML
	private Text techniekerLabel;

//	private FilteredList<Machine> filteredMachines;
//	private FilteredList<String> filteredDates;

	@FXML
	public void initialize() {
		
		this.sc = ServiceController.getInstance();

		reportPageController = new ReportPageController();
		rootLayout.setLeft(NavbarManager.getNavbar());

		User user = Session.getCurrentUser();
		Rollen userRole = user.getRol();

		plannedVBox.setSpacing(10);
		progressVBox.setSpacing(10);
		
		fullProgressVBox.setManaged(true);
		fullProgressVBox.setVisible(true);

		if (userRole == Rollen.TECHNIEKER) {
			planButton.setVisible(false);
			List<Maintenance> maintenances = sc.getMaintenanceByTechnieker(user.getFullName());
			addReportButton.setVisible(true);
			addReportButton.setManaged(true);
			editAndViewReportButton.setVisible(false);
			editAndViewReportButton.setVisible(false);
			fillVBox(maintenances);
		} else {
			List<Maintenance> maintenances = sc.getAllMaintenances();
			addReportButton.setVisible(false);
			addReportButton.setManaged(false);
			editAndViewReportButton.setVisible(false);
			editAndViewReportButton.setVisible(false);
			fillVBox(maintenances);
		}

		planButton.setOnAction(event -> {
			openPlanMenu();
			reasonField.clear();
			notesField.clear();
			submitButton.setText("Plan");
			submitButton.setOnAction(eventt -> {
				planMaintenance();
			});
		});

		cancelButton.setOnAction(event -> {
			closeMaintenanceMenu();
		});

		submitButton.setOnAction(event -> {
			planMaintenance();
		});

		addReportButton.setOnAction(event -> navToAddReportPage());
		
		completedButton.setOnAction(event -> {
			setupCompletedVBox();
		});

		fieldVBox.setVisible(false);
		setupMachineComboBox();
		setupDateComboBox();

		plannedScrollable.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
		progressScrollable.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
	}

	private void setupCompletedVBox() {
		fullProgressVBox.setVisible(false);
        VBox.setVgrow(fullProgressVBox, Priority.NEVER);
        fullProgressVBox.setManaged(false);
		planButton.setText("Go back");
        planButton.setOnAction(eventt -> initialize());
        List<Maintenance> maintenances = sc.getAllMaintenances();
        fillCompletedVBox(maintenances.stream().filter(m -> m.getCurrentStateString().equals("FinishedState")).collect(Collectors.toList()));

	}

	private void openPlanMenu() {

		fieldVBox.setVisible(true);
		techniekerField.setVisible(false);
		techniekerLabel.setVisible(false);
	}

	private void closeMaintenanceMenu() {
		fieldVBox.setVisible(false);
	}

	private void planMaintenance() {
		if (checkFields()) {
			sc.planMaintenance(machineField.getValue(), dateField.getValue().split("-"), null,
					reasonField.getText(), null, notesField.getText());
			machineField.setPromptText("");
			dateField.setPromptText("");
			reasonField.clear();
			notesField.clear();
			List<Maintenance> maintenancesList = sc.getAllMaintenances();
			fillVBox(maintenancesList);
		}
	}

	private void navToAddReportPage() {
		reportPageController.setOnderhoudSchermController(this);
		Session.setCurrentMaintenance(selectedMaintenance);
		SceneSwitcher.switchScene("/view/ReportPage.fxml");
	}
	
	private boolean checkFields() {
		if (machineField.getValue() == null || dateField.getValue() == null || dateField.getValue().isBlank()
				|| dateField.getValue().isEmpty() || reasonField.getText() == null || reasonField.getText().isBlank()
				|| reasonField.getText().isEmpty() || reasonField.getText() == "") {
			showAlert("Fill in required fields",
					"Required fields have to be filled in in order to plan or edit a maintenance",
					Alert.AlertType.ERROR);
			return false;
		}
		return true;
	}

	private void updateMaintenance(Maintenance maintenance) {
		if (checkFields()) {
			maintenance.setReason(reasonField.getText());
			maintenance.setRemarks(notesField.getText());
			sc.editMaintenance(maintenance);
			machineField.setPromptText("");
			dateField.setPromptText("");
			reasonField.clear();
			notesField.clear();
		}
	}
	
	private void fillCompletedVBox(List<Maintenance> maintenances) {
		plannedVBox.getChildren().clear();
		progressVBox.getChildren().clear();
		plannedScrollable.setContent(plannedVBox);
		progressScrollable.setContent(progressVBox);
		for (Maintenance maintenance : maintenances) {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/MaintenanceItem.fxml"));
			Parent element = null;
			try {
				element = fxmlLoader.load();
			} catch (IOException e) {
				e.printStackTrace();
			}

			MaintenanceElement controller = fxmlLoader.getController();
			controller.setText(maintenance.getMachineCode(), maintenance.getStartDate().toString());
			controller.setMaintenance(maintenance);
			plannedVBox.getChildren().add(element);
			
			controller.textBox.setOnMouseClicked(event -> {
				openPlanMenu();
				techniekerLabel.setVisible(true);
				techniekerField.setVisible(true);
				techniekerField.setText(maintenance.getNameTechnician());
				machineField.setPromptText(maintenance.getMachineCode());
				dateField.setPromptText(maintenance.getStartDate().toString());
				reasonField.setText(maintenance.getReason());
				notesField.setText(maintenance.getRemarks());
				cancelButton.setVisible(false);
				submitButton.setVisible(false);
				techniekerField.setEditable(false);
				machineField.setEditable(false);
				dateField.setEditable(false);
				reasonField.setEditable(false);
				notesField.setEditable(false);
				submitButton.setVisible(false);
			});
			controller.nextArrow.setVisible(false);
		}
		plannedScrollable.setContent(plannedVBox);
	}

	private void fillVBox(List<Maintenance> maintenances) {
		plannedVBox.getChildren().clear();
		progressVBox.getChildren().clear();
		plannedScrollable.setContent(plannedVBox);
		progressScrollable.setContent(progressVBox);
		for (Maintenance maintenance : maintenances) {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/MaintenanceItem.fxml"));
			Parent element = null;
			try {
				element = fxmlLoader.load();
			} catch (IOException e) {
				e.printStackTrace();
			}

			MaintenanceElement controller = fxmlLoader.getController();
			controller.setText(maintenance.getMachineCode(), maintenance.getStartDate().toString());
			controller.setMaintenance(maintenance);
			switch (maintenance.getCurrentStateString()) {
			case "PlannedState" -> {
				plannedVBox.getChildren().add(element);
			}
			case "ProgressState" -> {
				progressVBox.getChildren().add(element);
			}

			}
			controller.textBox.setOnMouseClicked(event -> {
				selectedMaintenance = maintenance;

				openPlanMenu();
				techniekerLabel.setVisible(true);
				techniekerField.setVisible(true);
				techniekerField.setText(maintenance.getNameTechnician());
				machineField.setPromptText(maintenance.getMachineCode());
				dateField.setPromptText(maintenance.getStartDate().toString());
				reasonField.setText(maintenance.getReason());
				notesField.setText(maintenance.getRemarks());
				cancelButton.setVisible(false);
				submitButton.setOnAction(eventt -> {
					updateMaintenance(maintenance);
				});
				techniekerField.setEditable(false);
				if (Session.getCurrentUser().getRol() == Rollen.TECHNIEKER) {

					machineField.setEditable(false);
					dateField.setEditable(false);
					reasonField.setEditable(false);
					notesField.setEditable(false);
					submitButton.setVisible(false);
				} else {
					submitButton.setText("Edit");
					submitButton.setVisible(true);
				}
			});

			controller.nextArrow.setOnMouseClicked(event -> {
				System.out.println("Clicked");
				if (maintenance.getMachine().getCurrentState() == "stopped") {
					switch (maintenance.getCurrentStateString()) {
					case "PlannedState" -> {
						maintenance.setCurrentState(new ProgressState(maintenance));
					}
					case "ProgressState" -> {
						maintenance.setCurrentState(new FinishedState(maintenance));
						maintenance.setEndDate(LocalDate.now());
					}
					}
				} else {
					showAlert("Machine not stopped",
							"Machine has to be in stopped state in order for a maintenance to be done",
							Alert.AlertType.ERROR);
				}
				sc.editMaintenance(maintenance);

				List<Maintenance> maintenancesList = sc.getAllMaintenances();
				fillVBox(maintenancesList);
			});

		}
		plannedScrollable.setContent(plannedVBox);
		progressScrollable.setContent(progressVBox);
	}

	private void setupMachineComboBox() {
		List<Machine> allMachines = sc.getAllMachines().stream().collect(Collectors.toList());

		if (allMachines.isEmpty()) {
			machineField.setDisable(true);
			machineField.setPromptText("No technicians available");
			return;
		}

		ObservableList<Machine> machines = FXCollections.observableArrayList(allMachines);

		filteredMachines = new FilteredList<>(machines, p -> true);

		machineField.setItems(machines);

		machineField.getStyleClass().add("comboBox");
		machineField.getStyleClass().add("filter-combo");

		machineField.setCellFactory(new Callback<ListView<Machine>, ListCell<Machine>>() {
			@Override
			public ListCell<Machine> call(ListView<Machine> param) {
				return new ListCell<Machine>() {
					@Override
					protected void updateItem(Machine item, boolean empty) {
						super.updateItem(item, empty);
						if (item == null || empty) {
							setText(null);
						} else {
							setText(item.getCode());
						}
					}
				};
			}
		});

		machineField.setConverter(new StringConverter<Machine>() {
			@Override
			public String toString(Machine machine) {
				return machine == null ? "" : machine.getCode();
			}

			@Override
			public Machine fromString(String string) {
				if (string == null || string.isEmpty()) {
					return null;
				}
				return machines.stream().filter(machine -> (machine.getCode()).equalsIgnoreCase(string)).findFirst()
						.orElse(null);
			}
		});

		machineField.setEditable(true);

		TextField editor = machineField.getEditor();

		final boolean[] isUpdatingFilter = new boolean[1];

		editor.textProperty().addListener((observable, oldValue, newValue) -> {
			if (isUpdatingFilter[0]) {
				return;
			}

			isUpdatingFilter[0] = true;
			try {
				filteredMachines.setPredicate(machine -> {
					if (newValue == null || newValue.isEmpty()) {
						return true;
					}

					String lowerCaseFilter = newValue.toLowerCase();

					return machine.getCode().toLowerCase().contains(lowerCaseFilter);
				});

				if (filteredMachines.size() > 0 && !newValue.isEmpty()) {
					if (!machineField.isShowing()) {
						machineField.show();
					}
				} else if (machineField.isShowing() && filteredMachines.isEmpty()) {
					machineField.hide();
				}
			} finally {
				isUpdatingFilter[0] = false;
			}
		});

		editor.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
			if (event.getCode() == KeyCode.DOWN || event.getCode() == KeyCode.UP || event.getCode() == KeyCode.ENTER) {
				return;
			}
		});

		machineField.setPromptText("Select or type to search");
	}

	private void setupDateComboBox() {
		List<String> allDates = new ArrayList<String>();
		allDates.add(LocalDate.now().plusDays(1).toString());
		allDates.add(LocalDate.now().plusDays(7).toString());
		allDates.add(LocalDate.now().plusDays(14).toString());

		ObservableList<String> dates = FXCollections.observableArrayList(allDates);

		filteredDates = new FilteredList<>(dates, p -> true);

		dateField.setItems(filteredDates);

		dateField.getStyleClass().add("comboBox");
		dateField.getStyleClass().add("filter-combo");

		dateField.setEditable(true);

		TextField editor = dateField.getEditor();

		final boolean[] isUpdatingFilter = new boolean[1];

		editor.textProperty().addListener((observable, oldValue, newValue) -> {
			if (isUpdatingFilter[0]) {
				return;
			}

			isUpdatingFilter[0] = true;
			try {
				filteredDates.setPredicate(date -> {
					if (newValue == null || newValue.isEmpty()) {
						return true;
					}

					String lowerCaseFilter = newValue.toLowerCase();
					return date.toLowerCase().contains(lowerCaseFilter);
				});

				if (filteredDates.size() > 0 && !newValue.isEmpty()) {
					if (!dateField.isShowing()) {
						dateField.show();
					}
				} else if (dateField.isShowing() && filteredDates.isEmpty()) {
					dateField.hide();
				}
			} finally {
				isUpdatingFilter[0] = false;
			}
		});

		dateField.valueProperty().addListener((obs, oldVal, newVal) -> {
			if (isUpdatingFilter[0] || newVal == null) {
				return;
			}

			isUpdatingFilter[0] = true;
			try {
				editor.setText(newVal);
				editor.positionCaret(editor.getText().length());
				editor.setStyle("-fx-text-fill: -deepBlue; -fx-font-weight: normal;");
			} finally {
				isUpdatingFilter[0] = false;
			}
		});

		editor.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
			if (event.getCode() == KeyCode.DOWN || event.getCode() == KeyCode.UP || event.getCode() == KeyCode.ENTER) {
				return;
			}
		});

	        dateField.setPromptText("Select or type to search");
	    }
	
	 private void showAlert(String title, String message, Alert.AlertType alertType) {
	        Alert alert = new Alert(alertType);
	        alert.setTitle(title);
	        alert.setHeaderText(null);
	        alert.setContentText(message);
	        alert.showAndWait();
	}

	public void updateButtons() {
		//addReportButton.setVisible(false);
        //addReportButton.setManaged(false);
        //editAndViewReportButton.setVisible(true);
        //editAndViewReportButton.setManaged(true);
	}	
}
