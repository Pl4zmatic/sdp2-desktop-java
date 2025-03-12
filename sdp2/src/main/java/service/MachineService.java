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

    public MachineService() {
        this.machineDao = new MachineDaoJpa();
    }

    public void update(Machine m){

            Machine existingMachine = machineDao.getMachineByCode(m.getCode());
            if (existingMachine != null) {
                MachineDaoJpa.startTransaction();
                machineDao.update(m);
                MachineDaoJpa.commitTransaction();
            } else {
                throw new EntityNotFoundException("Machine with code " + m.getCode() + " not found");
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
        } catch (Exception e) {
            e.printStackTrace();
            MachineDaoJpa.rollbackTransaction();
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

