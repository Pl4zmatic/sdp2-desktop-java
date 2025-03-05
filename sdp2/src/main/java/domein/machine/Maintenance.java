package domein.machine;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import utils.MaintenanceStatus;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "maintenances")
@AllArgsConstructor
public class Maintenance {
    
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
