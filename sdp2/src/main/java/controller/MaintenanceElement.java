package controller;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.text.Text;

public class MaintenanceElement {

    @FXML
    private Text dateLabel;

    @FXML
    private Text machineLabel;

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
}
