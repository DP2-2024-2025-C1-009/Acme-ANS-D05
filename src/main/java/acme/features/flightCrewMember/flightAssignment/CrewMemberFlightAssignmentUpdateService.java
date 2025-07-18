
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
	public void bind(final FlightAssignment flightAssignment) {
		Integer legId;
		Leg leg;

		legId = super.getRequest().getData("leg", int.class);
		leg = legId != null && legId != 0 ? this.assignmentRepository.findLegById(legId) : null;

		super.bindObject(flightAssignment, "duty", "status", "remarks");
		flightAssignment.setLeg(leg);
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
		data.put("leg", legChoices.getSelected() != null ? legChoices.getSelected().getKey() : null);
		data.put("crewMember", assignment.getCrewMember().getIdentity().getFullName());

		data.put("draftMode", assignment.getDraftMode());
		boolean legNotCompleted = true;
		if (assignment.getLeg() != null)
			legNotCompleted = !MomentHelper.isPast(assignment.getLeg().getScheduledArrival());
		data.put("legNotCompleted", legNotCompleted);

		super.getResponse().addData(data);
	}

}
