package controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import domein.Session;
import domein.machine.Maintenance;
import domein.machine.report.Image;
import domein.machine.report.Report;
import javafx.embed.swing.SwingFXUtils;
import javafx.beans.Observable;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import lombok.Getter;
import service.ImageService;
import service.ReportService;

public class ReportPageController {
    @FXML
    private BorderPane rootLayout;

    @FXML
    private Button addButton;

    @FXML
    private Button addImagesButton;

    @FXML
    private Button cancelButton;

    @FXML
    private TextArea procedureTextarea;

    @FXML
    private Button viewImagesButton;

    @FXML
    private ImageView viewSelectedImage;

    @FXML
    private ChoiceBox<String> choiceboxShowImage;

    @Getter private Report report;
    private ReportService reportService;
    private ImageService imageService;
    private OnderhoudSchermController onderhoudSchermController;

    @FXML
    private void initialize() {

        report = new Report();
        imageService = new ImageService();
        reportService = new ReportService();
        onderhoudSchermController = new OnderhoudSchermController();
        try {
            rootLayout.setLeft(NavbarManager.getNavbar());
        } catch (Exception e) {
            e.printStackTrace();
        }

        choiceboxShowImage.getItems().add("Kies een afbeelding");
        fillChoiceboxWithOptions();

        addButton.setOnAction(event -> addReport());
        cancelButton.setOnAction(event -> cancelReport());
        addImagesButton.setOnAction(event -> {
            addImageToReport();
            fillChoiceboxWithOptions();
        });
        choiceboxShowImage.setOnAction(event -> viewImages());
        procedureTextarea.setOnKeyTyped(event -> changeBorderToNormal());
    }

    private void changeBorderToNormal() {
        procedureTextarea.setBorder(null);
    }

    private void addReport() {
        if(procedureTextarea.getText() == null || procedureTextarea.getText().isEmpty() || procedureTextarea.getText().isBlank()) {
            procedureTextarea.setBorder(new Border(new BorderStroke(Color.rgb(239, 70, 60), 
            BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        } else {
            procedureTextarea.setBorder(null);
            reportService.addReport(report);
            Session.setCurrentReport(report);
            onderhoudSchermController.updateButtons();
            SceneSwitcher.switchScene("/view/OnderhoudScherm.fxml");
        }

    }

    private void cancelReport() {
        SceneSwitcher.switchScene("/view/OnderhoudScherm.fxml");
    }

    private void viewImages() {
        viewSelectedImage.setImage(null);
        for(Image image: report.getImages()) {
            if(image.getNameFile().equals(choiceboxShowImage.getValue())) {
                BufferedImage bufferedImage = null;
                try {
                    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(image.getData());
                    bufferedImage = ImageIO.read(byteArrayInputStream);
                    if(bufferedImage != null) {
                        WritableImage writableImage = SwingFXUtils.toFXImage(bufferedImage, null);
                        if(writableImage != null){
                            viewSelectedImage.setImage(writableImage);
                            viewSelectedImage.setPreserveRatio(true);
                            viewSelectedImage.setFitWidth(writableImage.getWidth());
                            viewSelectedImage.setFitHeight(writableImage.getHeight());
                        }
                    }
                } catch(IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void addImageToReport() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Png files", "*.png"),
            new FileChooser.ExtensionFilter("Jpeg files", "*.jpeg"), 
            new FileChooser.ExtensionFilter("Pdf files", "*.pdf"),
            new FileChooser.ExtensionFilter("Svg files", "*.svg"));
        File selectedFile = fileChooser.showOpenDialog(Window.getWindows().get(0));
        report.addImageFromPath(selectedFile.toPath().toString());
    }

    private void fillChoiceboxWithOptions() {
        choiceboxShowImage.getSelectionModel().selectFirst();
        for(Image image: report.getImages()) {
            if(!choiceboxShowImage.getItems().contains(image.getName())) {
                choiceboxShowImage.getItems().add(imageService.getImageNameFromPath(image.getName()));
            }
        }
    }

    public void setOnderhoudSchermController(OnderhoudSchermController controller) {
        this.onderhoudSchermController = controller;
    }
}
