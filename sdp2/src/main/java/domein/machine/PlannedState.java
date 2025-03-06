package domein.machine;

public class PlannedState extends MaintenanceState{

	public PlannedState(Maintenance maintenance) {
		super(maintenance);
	}
	
	public String executeMaintenance() {
		maintenance.setCurrentState(new ProgressState(maintenance));
		return "Het onderhoud wordt uitgevoerd";
	}
	
	

}
