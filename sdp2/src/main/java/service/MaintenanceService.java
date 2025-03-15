package service;

import repository.MachineDaoJpa;
import repository.MaintenanceDaoJpa;

public class MaintenanceService {
	private final MaintenanceDaoJpa maintenanceDaoJpa;
    private final UserService userService;
    private static MachineService instance;
    
    public MaintenanceService() {
    	this.maintenanceDaoJpa = new MaintenanceDaoJpa();
    	this.userService = UserService.getInstance();
    }
}
