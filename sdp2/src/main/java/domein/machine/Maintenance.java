package domein.machine;

import domein.machine.stateMachines.maintenance.MaintenanceState;
import domein.machine.stateMachines.maintenance.PlannedState;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

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

    @ManyToOne
    @JoinColumn(name = "machine_id", nullable = false)  // Deze kolom linkt Maintenance naar Machine
    private Machine machine;
    private LocalDateTime startDate;
    private LocalDateTime timeEnd;
    private String nameTechnician;
    private String reason;
    private String maintenanceReport;
    private String remark;
    private String currentStateString;
    @Transient
    private MaintenanceState currentState;
    
    public Maintenance(Machine machine,LocalDateTime startDate, String nameTechnician, 
    		String reason, String maintenanceReport, String remark) {
    	this.machine = machine;
    	this.startDate = startDate;
    	this.nameTechnician = nameTechnician;
    	this.reason = reason;
    	this.maintenanceReport = maintenanceReport;
    	this.remark = remark;
    	this.timeEnd = null;
    	this.currentState = new PlannedState(this);
    	this.currentStateString = currentState.toString();
    }
    
    public String getCurrentState(){
        return currentState.toString();
    }
}
