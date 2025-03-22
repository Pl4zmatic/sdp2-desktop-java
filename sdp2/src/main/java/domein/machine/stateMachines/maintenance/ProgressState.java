package domein.machine.stateMachines.maintenance;

import domein.machine.Maintenance;

public class ProgressState extends MaintenanceState {

	public ProgressState(Maintenance maintenance) {
		super(maintenance);
	}
	
	public String finishMaintenance() {
		maintenance.setCurrentState(new FinishedState(maintenance));
		return "Het onderhoud wordt uitgevoerd";
	}

	@Override
	public String toString() {
		return "ProgressState";
		
	}
}
