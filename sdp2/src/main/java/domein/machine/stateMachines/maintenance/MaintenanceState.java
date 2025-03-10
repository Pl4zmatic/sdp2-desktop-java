package domein.machine.stateMachines.maintenance;

import domein.machine.Maintenance;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;


@AllArgsConstructor
public abstract class MaintenanceState {
	
	protected final Maintenance maintenance;
	
	public String executeMaintenance() {
		return "Het onderhoud wordt uitgevoerd";
	}
	
	public String finishMaintenance() {
		return "Het onderhoud wordt uitgevoerd";
	}
	
}
