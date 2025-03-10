package domein.machine;

import domein.Session;
import domein.machine.Machine;
import domein.user.User;
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
        try {
            Machine existingMachine = machineDao.getMachineByCode(m.getCode());
            if (existingMachine != null) {
                MachineDaoJpa.startTransaction();
                machineDao.update(m);
                MachineDaoJpa.commitTransaction();
            } else {
                System.out.printf("no machine to update");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public List<Machine> getAllMachines() {
        return machineDao.getAllMachines();
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

