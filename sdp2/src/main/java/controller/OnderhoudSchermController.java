package controller;

import java.awt.Component;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import domein.Session;
import domein.user.User;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.virtualizedfx.enums.ScrollPaneEnums.ScrollBarPolicy;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import utils.Rollen;

public class OnderhoudSchermController {

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
		
		String[][] mockMaintenance =  {{"M01", "1/03/2025"}, {"M02", "1/03/2025"}};
		rootLayout.setLeft(NavbarManager.getNavbar());
		
		for(String[] textArray : mockMaintenance) {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass()
					.getResource("/view/MaintenanceItem.fxml"));
			Parent element = null;
			try {
				element = fxmlLoader.load();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			MaintenanceElement controller = fxmlLoader.getController();
			controller.setText(textArray[0], textArray[1]);
			plannedVBox.getChildren().add(element);
		}
		
		User user = Session.getCurrentUser();
		Rollen userRole = user.getRol();
		
		if(userRole == Rollen.TECHNIEKER) {
			planButton.setVisible(false);
		}
		
		planButton.setOnAction(event -> {
			openPlanMenu();
		});
		
		cancelButton.setOnAction(event -> {
			closeMaintenanceMenu();
		});
		
		fieldVBox.setVisible(false);
		
		
		plannedScrollable.setContent(plannedVBox);
		
		plannedScrollable.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);	
		progressScrollable.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
		
	}
	
	private void openPlanMenu() {
		fieldVBox.setVisible(true);
	}
	
	private void closeMaintenanceMenu() {
		fieldVBox.setVisible(false);
	}
}
