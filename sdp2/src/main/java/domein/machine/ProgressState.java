package domein.machine;

public class ProgressState extends MaintenanceState {

	public ProgressState(Maintenance maintenance) {
		super(maintenance);
	}
	
	public String finishMaintenance() {
		maintenance.setCurrentState(new FinishedState(maintenance));
		return "Het onderhoud wordt uitgevoerd";
	}

}
