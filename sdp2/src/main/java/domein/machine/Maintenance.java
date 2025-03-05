package domein.machine;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.eclipse.persistence.annotations.PrimaryKey;
import utils.MaintenanceStatus;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Entity
@Table(name = "maintenances")
@NoArgsConstructor
public class Maintenance {

    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int maintenanceId;
    @Transient
    private Machine machine;
    private LocalDate dateStart;
    private LocalTime timeStart;
    private LocalTime timeEnd;
    private String nameTechnician;
    private String reason;
    private String maintenanceReport;
    private String remark;
    private MaintenanceStatus state;
}
