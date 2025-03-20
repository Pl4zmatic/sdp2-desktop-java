package controller;

import java.awt.Component;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import domein.Session;
import domein.machine.Maintenance;
import domein.machine.stateMachines.maintenance.FinishedState;
import domein.machine.stateMachines.maintenance.MaintenanceState;
import domein.machine.stateMachines.maintenance.PlannedState;
import domein.machine.stateMachines.maintenance.ProgressState;
import domein.user.User;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.virtualizedfx.enums.ScrollPaneEnums.ScrollBarPolicy;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import service.MaintenanceService;
import utils.Rollen;

public class OnderhoudSchermController {

	private MaintenanceService maintenanceService;
	
	@FXML
	private BorderPane rootLayout;
	@FXML
	private MFXButton cancelButton;

	@FXML
    private VBox progressVBox;
	
    @FXML
    private VBox plannedVBox;
    
	@FXML
	private TextField dateField;

	@FXML
	private TextField machineField;

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
	public void initialize() {
		
		maintenanceService = new MaintenanceService();		
		rootLayout.setLeft(NavbarManager.getNavbar());
		
		List<Maintenance> maintenances = maintenanceService.getAllMaintenance();
		fillVBox(maintenances);
		
		User user = Session.getCurrentUser();
		Rollen userRole = user.getRol();
		
		if(userRole == Rollen.TECHNIEKER) {
			planButton.setVisible(false);
		}
		
		planButton.setOnAction(event -> {
			openPlanMenu();
			machineField.clear();
			dateField.clear();
			reasonField.clear();
			notesField.clear();
		});
		
		cancelButton.setOnAction(event -> {
			closeMaintenanceMenu();
		});
		
		submitButton.setOnAction(event -> {
			planMaintenance();
		});
		
		fieldVBox.setVisible(false);
		
		
		plannedScrollable.setContent(plannedVBox);
		progressScrollable.setContent(progressVBox);
		
		plannedScrollable.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);	
		progressScrollable.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
		
	}
	
	private void openPlanMenu() {
		fieldVBox.setVisible(true);
		techniekerField.setVisible(false);
	}
	
	private void closeMaintenanceMenu() {
		fieldVBox.setVisible(false);
	}
	
	private void planMaintenance() {
		maintenanceService.planMaintenance(machineField.getText(), dateField.getText().split("/"), null, reasonField.getText(), null, notesField.getText());
		machineField.clear();
		dateField.clear();
		reasonField.clear();
		notesField.clear();
	}
	
	private void fillVBox(List<Maintenance> maintenances) {
		for(Maintenance maintenance : maintenances) {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass()
					.getResource("/view/MaintenanceItem.fxml"));
			Parent element = null;
			try {
				element = fxmlLoader.load();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			MaintenanceElement controller = fxmlLoader.getController();
			controller.setText(maintenance.getMachineCode(), maintenance.getStartDate().toString());
			controller.setMaintenance(maintenance);
			switch(maintenance.getCurrentStateString()) {
			case "PlannedState" -> {
				plannedVBox.getChildren().add(element);
			}
			case "ProgressState" -> {
				progressVBox.getChildren().add(element);
			}
			
			}
			controller.textBox.setOnMouseClicked(event -> {
				openPlanMenu();
				techniekerField.setVisible(true);
				techniekerField.setText(maintenance.getNameTechnician());
				machineField.setText(maintenance.getMachineCode());
				dateField.setText(maintenance.getStartDate().toString());
				reasonField.setText(maintenance.getReason());
				notesField.setText(maintenance.getRemarks());
			});
			
			controller.nextArrow.setOnMouseClicked(event -> {
	    		System.out.println("Clicked");
	    		switch(maintenance.getCurrentStateString()) {
	    		case "PlannedState"-> {
	    			maintenance.setCurrentState(new ProgressState(maintenance));
	    		}
	    		case "ProgressState" -> {
	    			maintenance.setCurrentState(new FinishedState(maintenance));
	    		}
	    		}
	    		maintenanceService.editMaintenance(maintenance);
	    		plannedVBox.getChildren().clear();
	    		progressVBox.getChildren().clear();
	    		plannedScrollable.setContent(plannedVBox);
				progressScrollable.setContent(progressVBox);
				List<Maintenance> maintenancesList = maintenanceService.getAllMaintenance();
				fillVBox(maintenancesList);
				plannedScrollable.setContent(plannedVBox);
				progressScrollable.setContent(progressVBox);
	    	});
			
			
		}
	}

	
}
