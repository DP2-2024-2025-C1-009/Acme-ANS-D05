
package acme.features.flightCrewMember;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.realms.flightCrewMembers.FlightCrewMember;

@Repository
public interface FlightCrewMemberRepository extends AbstractRepository {

	Optional<FlightCrewMember> findOneMemberByEmployeeCode(String employeeCode);
	List<FlightCrewMember> findManyMembersByEmployeeCode(String employeeCode);
}
