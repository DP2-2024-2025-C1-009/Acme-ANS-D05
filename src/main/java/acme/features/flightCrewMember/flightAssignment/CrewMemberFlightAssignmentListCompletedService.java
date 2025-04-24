
package acme.features.flightCrewMember.flightAssignment;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flightAssignment.FlightAssignment;
import acme.realms.flightCrewMembers.FlightCrewMember;

@GuiService
public class CrewMemberFlightAssignmentListCompletedService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

	@Autowired
	private CrewMemberFlightAssignmentRepository assignmentRepository;


	@Override
	public void authorise() {
		boolean isCrew = super.getRequest().getPrincipal().hasRealmOfType(FlightCrewMember.class);
		super.getResponse().setAuthorised(isCrew);
	}

	@Override
	public void load() {
		FlightCrewMember crew = (FlightCrewMember) super.getRequest().getPrincipal().getActiveRealm();
		Collection<FlightAssignment> completedAssignments = this.assignmentRepository.findCompletedAssignments(crew.getId(), MomentHelper.getCurrentMoment());
		super.getBuffer().addData(completedAssignments);
	}

	@Override
	public void unbind(final FlightAssignment assignment) {
		Dataset data = super.unbindObject(assignment, "duty", "lastUpdate", "status", "remarks", "draftMode", "leg");
		super.getResponse().addData(data);
	}
}
