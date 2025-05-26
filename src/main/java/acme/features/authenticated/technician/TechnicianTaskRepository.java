
package acme.features.authenticated.technician;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.maintenance.Involves;
import acme.entities.maintenance.MaintenanceRecord;
import acme.entities.maintenance.Task;

@Repository
public interface TechnicianTaskRepository extends AbstractRepository {

	@Query("select t from Task t where t.technician.id = :technicianId")
	Collection<Task> findTasksByTechnicianId(int technicianId);

	@Query("select t from Task t where t.id = :id")
	Task findTaskById(int id);

	@Query("select i from Involves i where i.task.id = :id")
	Collection<Involves> findInvolvesByTaskId(int id);

	@Query("select t from Task t where t.ticker = :ticker")
	Task findTaskByTicker(String ticker);

	@Query("select m from MaintenanceRecord m where m.id = :masterId")
	MaintenanceRecord findMaintenanceRecordById(int masterId);

	@Query("select i.task from Involves i where i.maintenanceRecord.id = :masterId and i.task.technician.id = :technicianId")
	Collection<Task> findTasksByMaintenanceRecordId(int masterId, int technicianId);
}
