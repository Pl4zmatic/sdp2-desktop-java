package domein.machine;

import domein.machine.stateMachines.maintenance.MaintenanceState;
import domein.machine.stateMachines.maintenance.PlannedState;
import domein.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import service.MachineService;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "maintenances")
@NoArgsConstructor
public class Maintenance {

    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long maintenanceId;

    @ManyToOne
    @JoinColumn(name = "machine_id", nullable = false)  // Deze kolom linkt Maintenance naar Machine
    private Machine machine;
    private String machineCode;
    private LocalDate startDate;
    private LocalDate endDate;
    @Transient
    private User technician;
    private String nameTechnician;
    private String reason;
    private String maintenanceReport;
    private String remarks;
    private String currentStateString;
    @Transient
    private MaintenanceState currentState;
    
    @Transient
    private MachineService machineService;
    
    public Maintenance(String machineCode, LocalDate startDate, LocalDate endDate, 
    		String reason, String maintenanceReport, String remarks) {
    	machineService = new MachineService();
    	this.machine = machineService.getMachineByCode(machineCode);
    	setStartDate(startDate);
    	this.technician = machine.getTechnieker();
    	this.nameTechnician = machine.getTechniekerNaam();
        setReason(reason);
        setMaintenanceReport(maintenanceReport);
        setRemarks(remarks);
    	this.currentState = new PlannedState(this);
    	this.currentStateString = currentState.toString();
    	this.machineCode = machine.getCode();
    }
    
    public Maintenance(Maintenance m) {
    	machine = m.machine;
    	machineCode = m.machineCode;
    	startDate = m.startDate;
    	endDate = m.endDate;
    	technician = m.technician;
    	nameTechnician = m.nameTechnician;
    	reason = m.reason;
    	maintenanceReport = m.maintenanceReport;
    	remarks = m.remarks;
    	currentStateString = m.currentStateString;
    	currentState = m.currentState;
    }
    
    public void setCurrentState(MaintenanceState state) {
        this.currentState = state;
        this.currentStateString = state.toString();
    }

    public String getCurrentState(){
        return currentState.toString();
    }

    public void setStartDate(LocalDate startDate) {
        checkDateTime(startDate);

        this.startDate = startDate;
    }

    public void setEndDate(LocalDate endDate) {
        checkDateTime(endDate);

        if(endDate.isBefore(getStartDate())) {
            throw new DateTimeException("Het opgegeven eindtijdstip ligt voor de startdatum");
        }

        this.endDate = endDate;
    }

    public void setReason(String reason) {
        checkString(reason);
        this.reason = reason;
    }

    public void setMaintenanceReport(String maintenanceReport) {
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

    private void checkDateTime(LocalDate date) {
        if(date.isBefore(LocalDate.now())) {
            throw new DateTimeException("The datetime is from the past");
        }
    }

    @Override
    public String toString() {
        return String.format("|" + "%-18s|".repeat(8), String.valueOf(maintenanceId), currentStateString, String.valueOf(startDate), String.valueOf(endDate), nameTechnician, reason, remarks, String.valueOf(machine.getId()));
    }
}
