package domein.machine;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
public class Report {
  @Id
  @Column(unique = true, nullable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int rapportId;

  @Setter
  @Column(nullable = true)
  private List<String> imagePaths;

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
    setImagePaths(imagePaths);
    setMaintenance(maintenance);
    setNotes(notes);
    setSteps(steps);
  }

  @Override
  public String toString() {
    return String.format("|" + "%-18s|".repeat(5), String.valueOf(rapportId), imagePaths, steps, notes, String.valueOf(maintenance.getMaintenanceId()));
  }
}
