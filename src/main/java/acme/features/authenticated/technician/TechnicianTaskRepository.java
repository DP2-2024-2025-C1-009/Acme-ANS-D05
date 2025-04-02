
package acme.features.authenticated.technician;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.maintenance.Task;
import acme.realms.Technician;

@Repository
public interface TechnicianTaskRepository extends AbstractRepository {

	@Query("select t from Task t where t.technician.id = :technicianId")
	Collection<Task> findTasksByTechnicianId(int technicianId);

	@Query("select t from Task t where t.id = :id")
	Task findOneTaskById(int id);

	@Query("select te from Technician te where te.userAccount.id = :accountId")
	Technician findTechnicianByAccountId(int accountId);

	@Query("select t from Task t where t.draftMode = false")
	Collection<Task> findPublishedTasks();

}
