package controller;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

public class OnderhoudSchermController {

	@FXML
	private BorderPane rootLayout;
	@FXML
	private MFXButton cancelButton;

	@FXML
	private TextField dateField;

	@FXML
	private TextField machineField;

	@FXML
	private TextField notesField;

	@FXML
	private MFXButton planButton;

	@FXML
	private AnchorPane plannedScrollable;

	@FXML
	private AnchorPane progressScrollable;

	@FXML
	private TextField reasonField;

	@FXML
	private MFXButton submitButton;

	@FXML
	private TextField techniekerField;

	@FXML
	public void initialize() {
		rootLayout.setLeft(NavbarManager.getNavbar());
	}
}
