package repository;

import domein.machine.Machine;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.NoResultException;

import java.util.List;

public class MachineDaoJpa extends GenericDaoJpa<Machine> implements MachineDao {

    public MachineDaoJpa() {
        super(Machine.class);
    }

    @Override
    public Machine getMachineByCode(String code) {
        try {
            return em.createQuery("SELECT m FROM Machine m WHERE m.code = :code", Machine.class)
                    .setParameter("code", code)
                    .getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }



    public List<Machine> getAllMachines() {
        return em.createQuery("SELECT m FROM Machine m", Machine.class).getResultList();
    }

    public List<String> getAllLocations() {
        return em.createQuery("SELECT DISTINCT m.locatie FROM Machine m ORDER BY m.locatie", String.class)
                .getResultList();
    }


}
