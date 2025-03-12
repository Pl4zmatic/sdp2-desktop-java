package domein.machine;

import domein.machine.stateMachines.maintenance.MaintenanceState;
import domein.machine.stateMachines.maintenance.PlannedState;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.DateTimeException;
import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "maintenances")
@NoArgsConstructor
public class Maintenance {

    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int maintenanceId;

    @ManyToOne
    @JoinColumn(name = "machine_id", nullable = false)  // Deze kolom linkt Maintenance naar Machine
    private Machine machine;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String nameTechnician;
    private String reason;
    private String maintenanceReport;
    private String remarks;
    private String currentStateString;
    @Transient
    private MaintenanceState currentState;
    
    public Maintenance(Machine machine, LocalDateTime startDate, LocalDateTime endDate, String nameTechnician, 
    		String reason, String maintenanceReport, String remarks) {
    	this.machine = machine;
    	setStartDate(startDate);
        setEndDate(endDate);
    	setNameTechnician(nameTechnician);
        setReason(reason);
        setMaintenanceReport(maintenanceReport);
        setRemarks(remarks);
    	this.currentState = new PlannedState(this);
    	this.currentStateString = currentState.toString();
    }
    
    public void setCurrentState(MaintenanceState state) {
        this.currentState = state;
    }

    public String getCurrentState(){
        return currentState.toString();
    }

    public void setStartDate(LocalDateTime startDate) {
        checkDateTime(startDate);

        this.startDate = startDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        checkDateTime(endDate);

        if(endDate.isBefore(getStartDate())) {
            throw new DateTimeException("Het opgegeven eindtijdstip ligt voor de startdatum");
        }

        this.endDate = endDate;
    }

    public void setNameTechnician(String nameTechnician) {
        checkString(nameTechnician);
        this.nameTechnician = nameTechnician;
    }

    public void setReason(String reason) {
        checkString(reason);
        this.reason = reason;
    }

    public void setMaintenanceReport(String maintenanceReport) {
        checkString(maintenanceReport);
        this.maintenanceReport = maintenanceReport;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    private void checkString(String string) {
        if(string == null || string.isBlank() || string.isEmpty()) {
            throw new IllegalArgumentException(String.format("%s has to be filled in", string));
        }
    }

    private void checkDateTime(LocalDateTime datetime) {
        if(datetime.isBefore(LocalDateTime.now())) {
            throw new DateTimeException("The datetime is from the past");
        }
    }
}
