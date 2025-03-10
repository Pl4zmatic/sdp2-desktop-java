package domein.machine;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;

@Entity
@AllArgsConstructor
public abstract class MaintenanceState {
	
	@OneToOne
	protected final Maintenance maintenance;
	
	public String executeMaintenance() {
		return "Het onderhoud wordt uitgevoerd";
	}
	
	public String finishMaintenance() {
		return "Het onderhoud wordt uitgevoerd";
	}
	
}
