package service;

import domein.Session;
import domein.machine.Machine;
import domein.user.User;
import jakarta.persistence.EntityNotFoundException;
import repository.MachineDaoJpa;
import repository.UserDaoJpa;
import org.mindrot.jbcrypt.BCrypt;
import utils.Rollen;

import java.util.List;

public class MachineService {
    private final MachineDaoJpa machineDao;
    private final LogService logService;
    private static MachineService instance;
    public MachineService() {

        this.machineDao = new MachineDaoJpa();
        this.logService = LogService.getInstance();
    }
    public static MachineService getInstance() {
        if (instance == null) {
            instance = new MachineService();
        }
        return instance;
    }



    public void update(Machine m){

            Machine existingMachine = machineDao.getMachineByCode(m.getCode());

            if (existingMachine != null) {

                MachineDaoJpa.startTransaction();
                machineDao.update(m);
                MachineDaoJpa.commitTransaction();
                logService.logMachineEdit(existingMachine, m);

            } else {
                throw new EntityNotFoundException("Machine with code " + m.getCode() + " not found");
            }

    }

    public void deleteMachine(String code){
        Machine existingMachine = machineDao.getMachineByCode(code);

        if (existingMachine != null) {
            MachineDaoJpa.startTransaction();
            machineDao.softDelete(existingMachine);
            MachineDaoJpa.commitTransaction();
            logService.logMachineDelete(existingMachine);
        } else {
            throw new EntityNotFoundException("Machine with code " + code + " not found");
        }
    }

    public List<Machine> getAllMachines() {
        return machineDao.getAllMachines();
    }

    public void addMachine(Machine m) {
        try {
            MachineDaoJpa.startTransaction();
            machineDao.insert(m);
            MachineDaoJpa.commitTransaction();
            logService.logMachineCreation(m);
        } catch (Exception e) {

            MachineDaoJpa.rollbackTransaction();
            throw new RuntimeException(e);
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
}

