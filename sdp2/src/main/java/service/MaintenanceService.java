package service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import domein.Session;
import domein.machine.Machine;
import domein.machine.Maintenance;
import domein.site.Site;
import domein.machine.report.Report;
import domein.user.User;
import repository.MachineDaoJpa;
import repository.MaintenanceDaoJpa;
import repository.UserDaoJpa;

public class MaintenanceService {
	private final MaintenanceDaoJpa maintenanceDaoJpa;
    private static MaintenanceService instance;

    
    public MaintenanceService() {
    	this.maintenanceDaoJpa = new MaintenanceDaoJpa();

    }
    
    public static MaintenanceService getInstance() {
        if (instance == null) {
            instance = new MaintenanceService();
        }
        return instance;
    }
    
    public boolean planMaintenance(Machine machine, String[] startDate, LocalDate endDate,
    		String reason, String maintenanceReport, String remarks) {
    	try {
    		Maintenance newMaintenance = new Maintenance(machine,  LocalDate.of(Integer.parseInt(startDate[0]), Integer.parseInt(startDate[1]), Integer.parseInt(startDate[2])), endDate, reason, maintenanceReport, remarks);
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
	public List<Maintenance> getMaintenanceByTechnieker() {
		 try {
            return Collections.unmodifiableList(getMaintenanceForCurrentSite().stream().filter(m -> m.getNameTechnician().equals(Session.getCurrentUser().getFullName())).collect(Collectors.toList()));
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
	}
	
	public List<Maintenance> getMaintenanceForCurrentSite() {
        Site currentSite = Session.getCurrentSite();
        if (currentSite != null) {
            return machineService.getMachinesForCurrentSite().stream().flatMap(m -> m.getOnderhouden().stream()).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    public Maintenance getMaintenanceById(long id) {
        return maintenanceDaoJpa.get(id);
    }
}
