
package acme.features.flightCrewMember.flightAssignment;

import java.util.Collection;
import java.util.Date;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.activityLog.ActivityLog;
import acme.entities.flightAssignment.Duty;
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

	@Query("SELECT f FROM FlightAssignment f WHERE f.draftMode = false")
	Collection<FlightAssignment> findAllPublished();

	@Query("SELECT l FROM Leg l WHERE l.aircraft.airline.id = :airlineId")
	Collection<Leg> findLegsByAirline(int airlineId);

	@Query("SELECT log FROM ActivityLog log WHERE log.activityLogAssignment.id = :assignmentId")
	Collection<ActivityLog> findRelatedLogs(int assignmentId);

	@Query("SELECT f FROM FlightAssignment f WHERE f.leg.id = :legId AND f.duty = :duty")
	FlightAssignment findByLegAndDuty(int legId, Duty duty);

	@Query("SELECT COUNT(f) > 0 FROM FlightAssignment f WHERE f.leg.id = :legId AND f.duty IN ('PILOT', 'CO_PILOT') AND f.duty = :duty AND f.id <> :id")
	Boolean isPilotOrCoPilotDuplicated(int legId, Duty duty, int id);

	@Query("SELECT COUNT(f) > 0 FROM FlightAssignment f WHERE f.crewMember.id = :crewId AND f.lastUpdate >= :reference AND f.draftMode = false")
	Boolean isCrewMemberOverlapping(int crewId, Date reference);

	@Query("SELECT f FROM FlightAssignment f WHERE f.leg.flight.id = :flightId")
	Collection<FlightAssignment> findByFlight(int flightId);

	@Query("SELECT f FROM FlightAssignment f WHERE f.leg.id = :legId")
	Collection<FlightAssignment> findByLeg(int legId);
}
