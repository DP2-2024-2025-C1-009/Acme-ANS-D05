
package acme.features.flightCrewMember.flightAssignment;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flightAssignment.FlightAssignment;
import acme.realms.flightCrewMembers.FlightCrewMember;

@GuiService
@Service
public class CrewMemberFlightAssignmentListPlannedService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

	@Autowired
	protected CrewMemberFlightAssignmentRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		int accountId = super.getRequest().getPrincipal().getAccountId();
		FlightCrewMember principal = this.repository.findFlightCrewMemberByAccountId(accountId);
		int crewId = principal.getId();
		Date now = MomentHelper.getCurrentMoment();

		Collection<FlightAssignment> plannedAssignments = this.repository.findPlannedAssignmentsByMemberId(crewId);

		super.getBuffer().addData(plannedAssignments);
	}

	@Override
	public void unbind(final FlightAssignment assignment) {
		assert assignment != null;

		Dataset dataset = super.unbindObject(assignment, "lastUpdate", "status", "duty", "remarks");
		super.getResponse().addData(dataset);
	}
}
