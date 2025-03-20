package service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import domein.machine.Machine;
import domein.machine.Maintenance;
import domein.user.User;
import repository.MachineDaoJpa;
import repository.MaintenanceDaoJpa;
import repository.UserDaoJpa;

public class MaintenanceService {
	private final MaintenanceDaoJpa maintenanceDaoJpa;
    private static UserService instance;
    private final MachineService machineService;
    
    public MaintenanceService() {
    	this.maintenanceDaoJpa = new MaintenanceDaoJpa();
    	this.machineService = MachineService.getInstance();
    }
    
    public static UserService getInstance() {
        if (instance == null) {
            instance = new UserService();
        }
        return instance;
    }
    
    public boolean planMaintenance(String machineCode, String[] startDate, LocalDate endDate, 
    		String reason, String maintenanceReport, String remarks) {
    	try {
    		Maintenance newMaintenance = new Maintenance(machineCode, LocalDate.of(Integer.parseInt(startDate[0]), Integer.parseInt(startDate[1]), Integer.parseInt(startDate[2])), endDate, reason, maintenanceReport, remarks);
    		MaintenanceDaoJpa.startTransaction();
    		maintenanceDaoJpa.insert(newMaintenance);
    		MaintenanceDaoJpa.commitTransaction();
    		
    		return true;
    		
    	}catch(Exception e){
    		e.printStackTrace();
    		MaintenanceDaoJpa.rollbackTransaction();
    		return false;
    	}
    }
    
    public List<Maintenance> getAllMaintenance() {
    	 try {
             return Collections.unmodifiableList(maintenanceDaoJpa.findAll());
         } catch (Exception e) {
             e.printStackTrace();
             return Collections.emptyList();
         }
    }
    
    public boolean editMaintenance(Maintenance updatedMaintenance) {
        try {
            Maintenance existingMaintenance = maintenanceDaoJpa.getMaintenanceById(updatedMaintenance.getMaintenanceId());
            if (existingMaintenance != null) {
                MaintenanceDaoJpa.startTransaction();
                maintenanceDaoJpa.update(updatedMaintenance);
                MaintenanceDaoJpa.commitTransaction();
                return true;
            } else {
                System.out.println("Maintenance not found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            MaintenanceDaoJpa.rollbackTransaction();
        }
        return false;
    }
}
