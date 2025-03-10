package domein.machine;

import domein.Session;
import domein.machine.Machine;
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
        // machineDao.stopMachine(m);
    }
}
