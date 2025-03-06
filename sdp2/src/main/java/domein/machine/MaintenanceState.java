package domein.machine;

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
