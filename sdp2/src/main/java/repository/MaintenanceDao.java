package repository;

import java.util.List;

import domein.machine.Machine;
import domein.machine.Maintenance;
import domein.user.User;
import jakarta.persistence.EntityNotFoundException;

public interface MaintenanceDao extends GenericDao<Maintenance> {
	List<Maintenance> getMaintenancesByMachine(String machine) throws EntityNotFoundException;
	List<Maintenance> getMaintenancesByTechnician(String technician) throws EntityNotFoundException;
	Maintenance getMaintenanceById(Long id) throws EntityNotFoundException;
}
