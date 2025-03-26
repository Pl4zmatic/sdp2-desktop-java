package domein.machine.report;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import domein.machine.Maintenance;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import service.ImageService;

@Entity
//@NoArgsConstructor
@Getter
public class Report {
  @Id
  @Column(unique = true, nullable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int rapportId;
  
  @Column(nullable = true)
  private List<Image> images;

  @Setter
  @Column(nullable = false)
  private String steps;

  @Setter
  private Maintenance maintenance;

  public Report(List<String> imagePaths, String steps, Maintenance maintenance) {
    if (images == null)
      images = new ArrayList<>();
    setImagesFromStrings(imagePaths);
    setMaintenance(maintenance);
    setSteps(steps);
  }

  public Report(String steps, Maintenance maintenance) {
    if (images == null)
      images = new ArrayList<>();
    setMaintenance(maintenance);
    setSteps(steps);
  }

  public Report(String steps) {
    if (images == null)
      images = new ArrayList<>();
    setSteps(steps);
  }

  public Report() {
    if(images == null) {
      images = new ArrayList<>();
    }
    this.steps = "Stap 1";
  }

  public void setImagesFromStrings(List<String> imagePaths) {
    if (images == null)
      images = new ArrayList<>();

    ImageService imageService = new ImageService();
    for (String path : imagePaths) {
      Matcher fileNameMatcher = Pattern.compile("(?![/])[^./]*(?=[.])").matcher(path);
      Matcher extensionMatcher = Pattern.compile("[.].*$").matcher(path);

      Image image = new Image();
      image.setData(imageService.getImageBytesFromPath(path));

      if(fileNameMatcher.find())
        image.setName(fileNameMatcher.group(0));
      else throw new IllegalArgumentException("No name found for image.");

      if(extensionMatcher.find())
        image.setExtension(extensionMatcher.group(0));
      else throw new IllegalArgumentException("No extension found for image.");

      images.add(image);
    }
  }

  public void addImageFromPath(String imagePath) {

    ImageService imageService = new ImageService();
    Matcher fileNameMatcher = Pattern.compile("(?![/])[^./]*(?=[.])").matcher(imagePath);
    Matcher extensionMatcher = Pattern.compile("[.].*$").matcher(imagePath);

      Image image = new Image();
      image.setData(imageService.getImageBytesFromPath(imagePath));

      if(fileNameMatcher.find())
        image.setName(fileNameMatcher.group(0));
      else throw new IllegalArgumentException("No name found for image.");

      if(extensionMatcher.find())
        image.setExtension(extensionMatcher.group(0));
      else throw new IllegalArgumentException("No extension found for image.");

      images.add(image);
  }

  @Override
  public String toString() {
    return String.format("|%-18d|%-18d|%-18s|%-18d|",
            rapportId,
            (images != null ? images.size() : 0),
            (steps != null ? steps : "N/A"),
            (maintenance != null ? maintenance.getMaintenanceId() : 0)
    );
  }
}
