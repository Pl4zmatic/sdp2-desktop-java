package service;

import domein.Session;
import domein.machine.Machine;
import domein.site.Site;
import domein.user.User;
import jakarta.persistence.EntityNotFoundException;
import repository.MachineDaoJpa;
import repository.UserDaoJpa;
import org.mindrot.jbcrypt.BCrypt;
import utils.Rollen;

import java.util.Collections;
import java.util.List;

public class MachineService {
    private final MachineDaoJpa machineDao;

    private static MachineService instance;
    private MachineService() {

        this.machineDao = new MachineDaoJpa();

    }
    public static MachineService getInstance() {
        if (instance == null) {
            instance = new MachineService();
        }
        return instance;
    }



    public boolean update(Machine m){

            try {
                Machine existingMachine = machineDao.getMachineByCode(m.getCode());
                Machine clone = (Machine) existingMachine.clone();
                System.out.println(clone);
            if (existingMachine != null) {

                MachineDaoJpa.startTransaction();
                machineDao.update(m);
                MachineDaoJpa.commitTransaction();


                return true;
            } else {
                throw new EntityNotFoundException("Machine with code " + m.getCode() + " not found");
            }
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        return false;
    }

    public boolean deleteMachine(String code){
        Machine existingMachine = machineDao.getMachineByCode(code);

        if (existingMachine != null) {
            MachineDaoJpa.startTransaction();
            machineDao.softDelete(existingMachine);
            MachineDaoJpa.commitTransaction();


            return true;
        } else {
            throw new EntityNotFoundException("Machine with code " + code + " not found");
        }
    }

    public List<Machine> getAllMachines() {
        return machineDao.getAllMachines();
    }

    public boolean addMachine(Machine m) {
        try {
            MachineDaoJpa.startTransaction();
            machineDao.insert(m);
            MachineDaoJpa.commitTransaction();


            return true;
        } catch (Exception e) {

            MachineDaoJpa.rollbackTransaction();
            throw new RuntimeException(e);
        }
    }

    public List<Machine> getAllActiveMachines() {
        try {
            return Collections.unmodifiableList(machineDao.findAllActive());
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
    
    public Machine getMachineByCode(String code) {
    	try {
    		return machineDao.getMachineByCode(code);
    	}catch(Exception e) {
    		e.printStackTrace();
    		return null;
    	}
    }

    public void stopMachine(Machine m) {
        m.stopMachine();
        update(m);

    }

    public void setMachineInStartable(Machine m) {
        m.setMachineInStartable();
        update(m);
    }

    public void setMachineInMaintenance(Machine m) {
        m.setMachineInMaintenance();
        update(m);
    }

    public void startMachine(Machine m) {
        m.startMachine();
        update(m);
    }

    public List<String> getAllLocations(){
        return machineDao.getAllLocations();
    }

    public List<Machine> getMachinesBySite(String siteNaam) {
        try {
            return Collections.unmodifiableList(machineDao.getMachinesBySiteId(siteNaam));
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public List<Machine> getMachinesForCurrentSite() {
        Site currentSite = Session.getCurrentSite();
        if (currentSite != null) {
            return getMachinesBySite(currentSite.getName());
        }
        return Collections.emptyList();
    }
}

