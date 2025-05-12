
package acme.features.flightCrewMember.flightAssignment;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.activityLog.ActivityLog;
import acme.entities.flightAssignment.FlightAssignment;
import acme.entities.legs.Leg;

@Repository
public interface CrewMemberFlightAssignmentRepository extends AbstractRepository {

	@Query("SELECT f FROM FlightAssignment f WHERE f.id = :id")
	FlightAssignment findById(int id);

	@Query("SELECT f FROM FlightAssignment f WHERE f.crewMember.id = :crewId AND f.leg.scheduledDeparture >= :reference")
	Collection<FlightAssignment> findPlannedAssignments(int crewId, Date reference);

	@Query("SELECT f FROM FlightAssignment f WHERE f.crewMember.id = :crewId AND f.leg.scheduledArrival < :reference")
	Collection<FlightAssignment> findCompletedAssignments(int crewId, Date reference);

	@Query("SELECT log FROM ActivityLog log WHERE log.activityLogAssignment.id = :assignmentId")
	Collection<ActivityLog> findRelatedLogs(int assignmentId);

	@Query("select l from Leg l")
	List<Leg> findAllLegs();

	@Query("select l from Leg l WHERE l.scheduledArrival > :currentMoment AND l.draftMode = false")
	List<Leg> findPlannedPublishedLegs(Date currentMoment);

	@Query("select l from Leg l where l.id = :legId")
	Leg findLegById(int legId);

	@Query("select f from FlightAssignment f where f.crewMember.id = :crewId")
	List<FlightAssignment> findFlightAssignmentByCrewMemberId(int crewId);

	@Query("select f from FlightAssignment f where f.leg.id = :legId")
	List<FlightAssignment> findFlightAssignmentByLegId(int legId);

}
