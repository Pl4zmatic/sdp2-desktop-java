package repository;

import java.util.List;

import domein.machine.Machine;
import domein.machine.Maintenance;
import domein.user.User;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.NoResultException;

public class MaintenanceDaoJpa extends GenericDaoJpa<Maintenance> implements MaintenanceDao {

	public MaintenanceDaoJpa() {
		super(Maintenance.class);

}

	@Override
	public List<Maintenance> getMaintenancesByMachine(String machine) throws EntityNotFoundException {
		try {
			return em.createQuery("""
					SELECT m
					FROM Maintenance m
					WHERE m.machineCode = :machine
					""", Maintenance.class).setParameter("machine", machine).getResultList();
		}catch(NoResultException ex){
			return null;
		}
	}

	@Override
	public List<Maintenance> getMaintenancesByTechnician(String technician) throws EntityNotFoundException {
		try {
			return em.createQuery("""
					SELECT m
					FROM Maintenance m
					WHERE m.nameTechnician = :technician
					""", Maintenance.class).setParameter("technician", technician).getResultList();
		}catch(NoResultException ex){
			return null;
		}
	}

	@Override
	public Maintenance getMainentanceById(Long id) throws EntityNotFoundException {
		try {
			return em.createQuery("""
					SELECT m
					FROM Maintenance m
					WHERE m.maintenanceId = :id
					""", Maintenance.class).setParameter("id", id).getSingleResult();
		}catch(NoResultException ex){
			return null;
		}
		
	}
}
