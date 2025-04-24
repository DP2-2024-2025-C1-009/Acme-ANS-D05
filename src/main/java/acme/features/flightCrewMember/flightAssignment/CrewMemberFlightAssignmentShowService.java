
package acme.features.flightCrewMember.flightAssignment;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flightAssignment.AssignmentStatus;
import acme.entities.flightAssignment.Duty;
import acme.entities.flightAssignment.FlightAssignment;
import acme.entities.legs.Leg;
import acme.realms.flightCrewMembers.FlightCrewMember;

@GuiService
public class CrewMemberFlightAssignmentShowService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

	@Autowired
	private CrewMemberFlightAssignmentRepository assignmentRepository;


	@Override
	public void authorise() {
		int id = super.getRequest().getData("id", int.class);
		FlightAssignment assignment = this.assignmentRepository.findById(id);
		boolean authorised = assignment != null && (assignment.getCrewMember().getId() == super.getRequest().getPrincipal().getActiveRealm().getId() || !assignment.getDraftMode());
		super.getResponse().setAuthorised(authorised);
	}

	@Override
	public void load() {
		int id = super.getRequest().getData("id", int.class);
		FlightAssignment assignment = this.assignmentRepository.findById(id);
		super.getBuffer().addData(assignment);
	}

	@Override
	public void unbind(final FlightAssignment assignment) {
		Collection<Leg> legs;
		SelectChoices legChoices;
		SelectChoices dutyChoices;
		SelectChoices statusChoices;
		Dataset data;

		if (assignment.getDraftMode())
			legs = this.assignmentRepository.findPlannedPublishedLegs(MomentHelper.getCurrentMoment());
		else
			legs = this.assignmentRepository.findAllLegs();

		dutyChoices = SelectChoices.from(Duty.class, assignment.getDuty());
		statusChoices = SelectChoices.from(AssignmentStatus.class, assignment.getStatus());
		legChoices = SelectChoices.from(legs, "flightNumber", assignment.getLeg());

		data = super.unbindObject(assignment, "duty", "lastUpdate", "status", "remarks", "draftMode");

		data.put("dutyChoices", dutyChoices);
		data.put("statusChoices", statusChoices);
		data.put("legChoices", legChoices);
		data.put("leg", legChoices.getSelected().getKey());

		data.put("crewMember", assignment.getCrewMember().getIdentity().getFullName());

		data.put("legNotCompleted", MomentHelper.isFuture(assignment.getLeg().getScheduledArrival()));

		super.getResponse().addData(data);
	}
}
