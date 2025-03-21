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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import service.ImageService;

@Entity
@NoArgsConstructor
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
  private List<String> steps;

  @Setter
  @Column(length = 500, nullable = true)
  private String notes;

  @Setter
  @OneToOne
  private Maintenance maintenance;

  public Report(List<String> imagePaths, List<String> steps, String notes, Maintenance maintenance) {
    setImagesFromStrings(imagePaths);
    setMaintenance(maintenance);
    setNotes(notes);
    setSteps(steps);
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

  @Override
  public String toString() {
    return String.format("|" + "%-18s|".repeat(5), String.valueOf(rapportId), String.valueOf(images.size()), steps,
        notes, String.valueOf(maintenance.getMaintenanceId()));
  }
}
