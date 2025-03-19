package controller;

import domein.machine.Report;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;

public class ReportPageController {
  @FXML
  private BorderPane rootLayout;

  private Report report;

  @FXML
  private void initialize() {
    try {
      report = new Report();
      rootLayout.setLeft(NavbarManager.getNavbar());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }


}
