package controller;

import java.io.IOException;

import domein.machine.Maintenance;
import domein.machine.stateMachines.maintenance.FinishedState;
import domein.machine.stateMachines.maintenance.MaintenanceState;
import domein.machine.stateMachines.maintenance.ProgressState;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import service.MaintenanceService;

public class MaintenanceElement {

	private MaintenanceService maintenanceService;
	
    @FXML
    private Text dateLabel;
    
    @FXML
    public VBox textBox;

    @FXML
    private Text machineLabel;
    
    @FXML
    public VBox nextArrow;
    
    private Maintenance maintenance;
    
    @FXML
    public void intialize() {
    	maintenanceService = new MaintenanceService();	
    	FXMLLoader fxmlLoader = new FXMLLoader(getClass()
    			.getResource("/view/MaintenanceItem.fxml"));
    	
    	fxmlLoader.setRoot(this);
    	fxmlLoader.setController(this);
    	
    	try {
    		fxmlLoader.load();
    	}catch(IOException e) {
    		throw new RuntimeException(e);
    	}
    	
    }
    
    public void setText(String textMachine, String textDate){
    	machineLabel.setText(textMachine);
    	dateLabel.setText(textDate);
    }
    
    public void setMaintenance(Maintenance maintenance) {
    	this.maintenance = maintenance;
    }
    
    public Maintenance getMaintenance() {
    	return this.maintenance;
    }
        
}
