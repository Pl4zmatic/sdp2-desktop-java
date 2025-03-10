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

<<<<<<< HEAD
    public void stopMachine(Machine m) {

        machineDao.update(m);
=======


    public void stopMachine(Machine m) {
        // machineDao.stopMachine(m);
>>>>>>> origin/main
    }
}
