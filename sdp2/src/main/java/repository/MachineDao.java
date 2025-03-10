package repository;

import domein.machine.Machine;
import jakarta.persistence.EntityNotFoundException;

public interface MachineDao extends GenericDao<Machine> {
    Machine getMachineByCode(String code) throws EntityNotFoundException;

}
