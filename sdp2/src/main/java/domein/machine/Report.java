package domein.machine;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Transient;
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


  @Lob
  @Column(nullable = true)
  private List<byte[]> images;

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
    if(images == null)
      images = new ArrayList<>();

    ImageService imageService = new ImageService();
    for(String path : imagePaths) {
      images.add(imageService.getImageBytesFromPath(path));
    }
  }

  @Override
  public String toString() {
    return String.format("|" + "%-18s|".repeat(5), String.valueOf(rapportId), String.valueOf(images.size()), steps, notes, String.valueOf(maintenance.getMaintenanceId()));
  }
}
