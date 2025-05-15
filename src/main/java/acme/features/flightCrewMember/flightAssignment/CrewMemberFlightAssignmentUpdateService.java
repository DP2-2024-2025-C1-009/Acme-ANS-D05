
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
public class CrewMemberFlightAssignmentUpdateService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

	@Autowired
	private CrewMemberFlightAssignmentRepository assignmentRepository;


	@Override
	public void authorise() {
		boolean canEdit;

		int id = super.getRequest().getData("id", int.class);
		FlightAssignment assignment = this.assignmentRepository.findById(id);

		boolean draft = assignment != null && assignment.getDraftMode();
		boolean futureLeg = true;
		boolean legPublished = true;

		Integer legId = super.getRequest().getData("leg", int.class);
		if (legId != 0) {
			Leg leg = this.assignmentRepository.findLegById(legId);
			futureLeg = leg != null && !MomentHelper.isPast(leg.getScheduledArrival());
			legPublished = leg != null && !leg.isDraftMode();
		}

		FlightCrewMember crew = (FlightCrewMember) super.getRequest().getPrincipal().getActiveRealm();
		boolean correctCrew = assignment != null && assignment.getCrewMember().getId() == crew.getId();

		canEdit = draft && correctCrew && futureLeg && legPublished;

		super.getResponse().setAuthorised(canEdit);
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
		this.assignmentRepository.save(assignment);
	}

	@Override
	public void unbind(final FlightAssignment assignment) {
		List<Leg> legs = this.assignmentRepository.findPlannedPublishedLegs(MomentHelper.getCurrentMoment());

		SelectChoices legChoices;
		try {
			legChoices = SelectChoices.from(legs, "flightNumber", assignment.getLeg());
		} catch (Exception e) {
			legChoices = SelectChoices.from(legs, "flightNumber", new Leg());
		}

		SelectChoices dutyChoices = SelectChoices.from(Duty.class, assignment.getDuty());
		SelectChoices statusChoices = SelectChoices.from(AssignmentStatus.class, assignment.getStatus());

		Dataset data = super.unbindObject(assignment, "duty", "status", "remarks", "draftMode");

		data.put("readonly", false);
		data.put("moment", assignment.getLastUpdate());
		data.put("dutyChoices", dutyChoices);
		data.put("duty", dutyChoices.getSelected().getKey());
		data.put("statusChoices", statusChoices);
		data.put("assignmentStatus", statusChoices.getSelected().getKey());
		data.put("legChoices", legChoices);
		data.put("leg", legChoices.getSelected().getKey());
		data.put("crewMember", assignment.getCrewMember().getIdentity().getFullName());

		data.put("draftMode", assignment.getDraftMode());
		data.put("legNotCompleted", !MomentHelper.isPast(assignment.getLeg().getScheduledArrival()));

		super.getResponse().addData(data);
	}

}
