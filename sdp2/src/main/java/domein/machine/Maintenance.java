package domein.machine;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import utils.MaintenanceStatus;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
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
