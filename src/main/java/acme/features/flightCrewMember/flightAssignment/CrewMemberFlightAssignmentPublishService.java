
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
public class CrewMemberFlightAssignmentPublishService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

	@Autowired
	private CrewMemberFlightAssignmentRepository assignmentRepository;


	@Override
	public void authorise() {
		int id = super.getRequest().getData("id", int.class);
		FlightAssignment assignment = this.assignmentRepository.findById(id);
		boolean correctCrew = assignment != null && assignment.getCrewMember() != null && super.getRequest().getPrincipal().hasRealm(assignment.getCrewMember());
		boolean draft = assignment != null && assignment.getDraftMode();

		super.getResponse().setAuthorised(draft && correctCrew);
	}

	@Override
	public void load() {
		int id = super.getRequest().getData("id", int.class);
		FlightAssignment assignment = this.assignmentRepository.findById(id);
		assignment.setLastUpdate(MomentHelper.getCurrentMoment());
		super.getBuffer().addData(assignment);
	}

	@Override
	public void bind(final FlightAssignment assignment) {
		Integer legId = super.getRequest().getData("leg", int.class);
		Leg leg = this.assignmentRepository.findLegById(legId);

		super.bindObject(assignment, "duty", "status", "remarks");
		assignment.setLeg(leg);
	}

	@Override
	public void validate(final FlightAssignment assignment) {

	}

	@Override
	public void perform(final FlightAssignment assignment) {
		assignment.setDraftMode(false);
		this.assignmentRepository.save(assignment);
	}

	@Override
	public void unbind(final FlightAssignment assignment) {
		List<Leg> legs = this.assignmentRepository.findPlannedPublishedLegs(MomentHelper.getCurrentMoment());

		if (assignment.getLeg() != null && !legs.contains(assignment.getLeg()))
			legs.add(assignment.getLeg());

		SelectChoices legChoices = SelectChoices.from(legs, "flightNumber", assignment.getLeg());
		SelectChoices dutyChoices = SelectChoices.from(Duty.class, assignment.getDuty());
		SelectChoices statusChoices = SelectChoices.from(AssignmentStatus.class, assignment.getStatus());

		Dataset data = super.unbindObject(assignment, "duty", "status", "remarks", "draftMode");

		data.put("readonly", false);
		data.put("moment", assignment.getLastUpdate());

		data.put("duty", assignment.getDuty() != null ? assignment.getDuty().name() : "");
		data.put("dutyChoices", dutyChoices);

		data.put("assignmentStatus", assignment.getStatus() != null ? assignment.getStatus().name() : "");
		data.put("statusChoices", statusChoices);

		data.put("leg", assignment.getLeg() != null ? assignment.getLeg().getId() : "");
		data.put("legChoices", legChoices);

		data.put("crewMember", assignment.getCrewMember() != null ? assignment.getCrewMember().getIdentity().getFullName() : "N/A");

		boolean legNotCompleted = assignment.getLeg() == null || !MomentHelper.isPast(assignment.getLeg().getScheduledArrival());
		data.put("legNotCompleted", legNotCompleted);

		super.getResponse().addData(data);
	}

}
