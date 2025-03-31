
package acme.features.authenticated.flightCrewMember.flightAssignment;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.flightAssignment.FlightAssignment;
import acme.entities.legs.Leg;
import acme.realms.flightCrewMembers.FlightCrewMember;

@Repository
public interface CrewMemberFlightAssignmentRepository extends AbstractRepository {

	@Query("select fa from FlightAssignment fa where fa.leg.scheduledArrival < CURRENT_TIMESTAMP and fa.crewMember.id = :memberId")
	Collection<FlightAssignment> findCompletedAssignmentsByMemberId(int memberId);

	@Query("select fa from FlightAssignment fa where fa.leg.scheduledDeparture >= CURRENT_TIMESTAMP and fa.crewMember.id = :memberId")
	Collection<FlightAssignment> findPlannedAssignmentsByMemberId(int memberId);

	@Query("select fa from FlightAssignment fa where fa.id = :id")
	FlightAssignment findOneAssignmentById(int id);

	@Query("select fa.crewMember from FlightAssignment fa where fa.leg.id = :legId")
	Collection<FlightCrewMember> findCrewMembersByLegId(int legId);

	@Query("select fa.leg from FlightAssignment fa where fa.id = :assignmentId")
	Leg findLegByAssignmentId(int assignmentId);

	@Query("select l from Leg l where l.id = :legId")
	Leg findLegById(int legId);

	@Query("select l from Leg l")
	List<Leg> findAllLegs();

	@Query("select m from FlightCrewMember m where m.userAccount.id = :accountId")
	FlightCrewMember findFlightCrewMemberByAccountId(int accountId);

	@Query("select fa from FlightAssignment fa where fa.crewMember.id = :crewMemberId")
	Collection<FlightAssignment> findAssignmentsByCrewMemberId(int crewMemberId);

	@Query("select fa from FlightAssignment fa where fa.leg.id = :legId")
	Collection<FlightAssignment> findAssignmentsByLegId(int legId);

}
