
package acme.features.flightCrewMember.activityLog;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.activityLog.ActivityLog;
import acme.entities.flightAssignment.FlightAssignment;

@Repository
public interface ActivityLogRepository extends AbstractRepository {

	@Query("select fa from FlightAssignment fa where fa.id = :id")
	FlightAssignment findFlightAssignmentById(int id);

	@Query("select log from ActivityLog log where log.id = :id")
	ActivityLog findActivityLogById(int id);

	@Query("select log from ActivityLog log")
	Collection<ActivityLog> findAllActivityLogs();

	@Query("select log from ActivityLog log where log.activityLogAssignment.id = :assignmentId")
	Collection<ActivityLog> findLogsByAssignmentId(int assignmentId);
}
