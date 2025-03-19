package domein.machine.stateMachines.maintenance;

import domein.machine.Maintenance;

public class FinishedState extends MaintenanceState {

	public FinishedState(Maintenance maintenance) {
		super(maintenance);
	}

	@Override
	public String toString() {
		return "FinishedState";
		
	}
}
