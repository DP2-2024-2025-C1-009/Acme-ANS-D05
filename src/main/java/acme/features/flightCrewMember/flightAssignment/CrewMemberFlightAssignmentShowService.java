
package acme.features.flightCrewMember.flightAssignment;

import java.util.List;

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
		List<Leg> legs = this.assignmentRepository.findPlannedPublishedLegs(MomentHelper.getCurrentMoment());

		if (assignment.getLeg() != null && !legs.contains(assignment.getLeg()))
			legs.add(assignment.getLeg());

		SelectChoices legChoices = SelectChoices.from(legs, "flightNumber", assignment.getLeg());
		SelectChoices dutyChoices = SelectChoices.from(Duty.class, assignment.getDuty());
		SelectChoices statusChoices = SelectChoices.from(AssignmentStatus.class, assignment.getStatus());

		Dataset data = super.unbindObject(assignment, "status", "remarks");

		data.put("readonly", false);
		data.put("moment", assignment.getLastUpdate());
		data.put("duty", dutyChoices.getSelected() != null ? dutyChoices.getSelected().getKey() : "");
		data.put("dutyChoices", dutyChoices);
		data.put("assignmentStatus", statusChoices.getSelected() != null ? statusChoices.getSelected().getKey() : "");
		data.put("statusChoices", statusChoices);
		data.put("leg", legChoices.getSelected() != null ? legChoices.getSelected().getKey() : "");
		data.put("legChoices", legChoices);
		data.put("crewMember", assignment.getCrewMember().getIdentity().getFullName());

		data.put("draftMode", assignment.getDraftMode());
		data.put("legNotCompleted", assignment.getLeg() == null || !MomentHelper.isPast(assignment.getLeg().getScheduledArrival()));

		super.getResponse().addData(data);
	}

}
