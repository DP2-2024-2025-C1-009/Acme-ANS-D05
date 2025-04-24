
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
		boolean isAuthorised;

		int id = super.getRequest().getData("id", int.class);
		FlightAssignment assignment = this.assignmentRepository.findById(id);
		boolean draft = assignment != null && assignment.getDraftMode();

		Integer legId = super.getRequest().getData("leg", int.class);
		boolean futureLeg = true;
		boolean legPublished = true;
		if (legId != 0) {
			Leg leg = this.assignmentRepository.findLegById(legId);
			futureLeg = leg != null && !MomentHelper.isPast(leg.getScheduledArrival());
			legPublished = leg != null && !leg.isDraftMode();
		}

		FlightCrewMember crew = (FlightCrewMember) super.getRequest().getPrincipal().getActiveRealm();
		boolean correctCrew = assignment != null && assignment.getCrewMember().getId() == crew.getId();

		isAuthorised = draft && correctCrew && futureLeg && legPublished;
		super.getResponse().setAuthorised(isAuthorised);
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
		Dataset data = super.unbindObject(assignment, "duty", "lastUpdate", "status", "remarks", "draftMode", "crewMember", "leg");

		List<Leg> legs = this.assignmentRepository.findPlannedPublishedLegs(MomentHelper.getCurrentMoment());
		SelectChoices legChoices = SelectChoices.from(legs, "flightNumber", assignment.getLeg());

		SelectChoices dutyChoices = SelectChoices.from(Duty.class, assignment.getDuty());
		SelectChoices statusChoices = SelectChoices.from(AssignmentStatus.class, assignment.getStatus());

		data = super.unbindObject(assignment, "duty", "status", "remarks", "draftMode");

		data.put("confirmation", false);
		data.put("readonly", false);
		data.put("moment", assignment.getLastUpdate());
		data.put("duty", dutyChoices.getSelected().getKey());
		data.put("dutyChoices", dutyChoices);
		data.put("assignmentStatus", statusChoices.getSelected().getKey());
		data.put("statusChoices", statusChoices);
		data.put("leg", legChoices.getSelected().getKey());
		data.put("legChoices", legChoices);
		data.put("crewMember", assignment.getCrewMember().getIdentity().getFullName());
		super.getResponse().addData(data);
	}
}
