package controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import lombok.Getter;
import service.ImageService;
import service.ReportService;
import service.ServiceController;

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

    @FXML
    private Button deleteImageButton;

    @FXML
    private VBox imageControlsContainer;

    @FXML
    private VBox imageViewContainer;

    @Getter private Report report;
    @Getter private List<Image> reportImages;
    private ServiceController sc;
    private OnderhoudSchermController onderhoudSchermController;

    @FXML
    private void initialize() {
        reportImages = new ArrayList<>();
        sc = ServiceController.getInstance();
        onderhoudSchermController = new OnderhoudSchermController();
        try {
            rootLayout.setLeft(NavbarManager.getNavbar());
        } catch (Exception e) {
            e.printStackTrace();
        }

        imageControlsContainer.setVisible(false);
        imageControlsContainer.setManaged(false);
        imageViewContainer.setVisible(false);
        imageViewContainer.setManaged(false);

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

        viewImagesButton.setOnAction(event -> toggleImageControls());
        deleteImageButton.setOnAction(event -> deleteSelectedImage());
    }

    private void toggleImageControls() {
        boolean isVisible = !imageControlsContainer.isVisible();
        imageControlsContainer.setVisible(isVisible);
        imageControlsContainer.setManaged(isVisible);
        imageViewContainer.setVisible(isVisible);
        imageViewContainer.setManaged(isVisible);

        if (isVisible) {
            viewImagesButton.setText("Verberg afbeeldingen");
        } else {
            viewImagesButton.setText("Bekijk afbeeldingen");
        }
    }

    private void deleteSelectedImage() {
        String selectedImageName = choiceboxShowImage.getValue();
        if (selectedImageName != null && !selectedImageName.equals("Kies een afbeelding")) {
            reportImages.removeIf(image -> image.getNameFile().equals(selectedImageName));

            choiceboxShowImage.getItems().remove(selectedImageName);
            choiceboxShowImage.getSelectionModel().selectFirst();

            viewSelectedImage.setImage(null);
        }
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

            Maintenance maintenance = Session.getCurrentMaintenance();
            if (maintenance != null) {
                Report report;

                if (!reportImages.isEmpty()) {
                    List<String> imagePaths = new ArrayList<>();
                    for (Image image : reportImages) {
                        imagePaths.add(image.getName() + image.getExtension());
                    }
                    report = new Report(imagePaths, procedureTextarea.getText(), maintenance);
                } else {
                    report = new Report(procedureTextarea.getText(), maintenance);
                }

                sc.addReport(report);
                Session.setCurrentReport(report);
                onderhoudSchermController.updateButtons();
                SceneSwitcher.switchScene("/view/OnderhoudScherm.fxml");
            } else {
                procedureTextarea.setBorder(new Border(new BorderStroke(Color.rgb(239, 70, 60),
                        BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
            }
        }
    }

    private void cancelReport() {
        SceneSwitcher.switchScene("/view/OnderhoudScherm.fxml");
    }

    private void viewImages() {
        viewSelectedImage.setImage(null);
        String selectedImage = choiceboxShowImage.getValue();

        if (selectedImage == null || selectedImage.equals("Kies een afbeelding")) {
            return;
        }

        for(Image image: reportImages) {
            if(image.getNameFile().equals(selectedImage)) {
                BufferedImage bufferedImage = null;
                try {
                    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(image.getData());
                    bufferedImage = ImageIO.read(byteArrayInputStream);
                    if(bufferedImage != null) {
                        WritableImage writableImage = SwingFXUtils.toFXImage(bufferedImage, null);
                        if(writableImage != null){
                            viewSelectedImage.setImage(writableImage);
                            viewSelectedImage.setPreserveRatio(true);

                            double maxWidth = 400;
                            double maxHeight = 300;

                            if (writableImage.getWidth() > maxWidth || writableImage.getHeight() > maxHeight) {
                                if (writableImage.getWidth() / maxWidth > writableImage.getHeight() / maxHeight) {
                                    viewSelectedImage.setFitWidth(maxWidth);
                                    viewSelectedImage.setFitHeight(0);
                                } else {
                                    viewSelectedImage.setFitHeight(maxHeight);
                                    viewSelectedImage.setFitWidth(0);
                                }
                            } else {
                                viewSelectedImage.setFitWidth(writableImage.getWidth());
                                viewSelectedImage.setFitHeight(writableImage.getHeight());
                            }
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

        if (selectedFile != null) {
            String path = selectedFile.toPath().toString();

            Matcher fileNameMatcher = Pattern.compile("(?![/])[^./]*(?=[.])").matcher(path);
            Matcher extensionMatcher = Pattern.compile("[.].*$").matcher(path);

            Image image = new Image();
            image.setData(sc.getImageBytesFromPath(path));

            if(fileNameMatcher.find())
                image.setName(fileNameMatcher.group(0));
            else
                throw new IllegalArgumentException("No name found for image.");

            if(extensionMatcher.find())
                image.setExtension(extensionMatcher.group(0));
            else
                throw new IllegalArgumentException("No extension found for image.");

            reportImages.add(image);

            if (!imageControlsContainer.isVisible()) {
                toggleImageControls();
            }
        }
    }

    private void fillChoiceboxWithOptions() {
        choiceboxShowImage.getItems().clear();
        choiceboxShowImage.getItems().add("Kies een afbeelding");

        for(Image image: reportImages) {
            choiceboxShowImage.getItems().add(image.getNameFile());
        }

        choiceboxShowImage.getSelectionModel().selectFirst();
    }

    public void setOnderhoudSchermController(OnderhoudSchermController controller) {
        this.onderhoudSchermController = controller;
    }
}

