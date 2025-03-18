package controller;

import java.io.IOException;

import domein.machine.Maintenance;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.text.Text;

public class MaintenanceElement {

    @FXML
    private Text dateLabel;

    @FXML
    private Text machineLabel;
    
    private Maintenance maintenance;

    @FXML
    public void intialize() {
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
