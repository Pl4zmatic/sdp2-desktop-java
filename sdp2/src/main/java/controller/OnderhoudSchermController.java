package controller;

import java.awt.Component;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.virtualizedfx.enums.ScrollPaneEnums.ScrollBarPolicy;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

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
	private MFXButton submitButton;

	@FXML
	private TextField techniekerField;

	@FXML
	public void initialize() {
		rootLayout.setLeft(NavbarManager.getNavbar());
		FXMLLoader fxmlLoader = new FXMLLoader(getClass()
    			.getResource("/view/MaintenanceItem.fxml"));
		
		plannedScrollable.setVbarPolicy(javafx.scene.control.ScrollPane.ScrollBarPolicy.AS_NEEDED);	
		try {
			plannedVBox.getChildren().add(fxmlLoader.load());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
