
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
public class CrewMemberFlightAssignmentCreateService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

	@Autowired
	private CrewMemberFlightAssignmentRepository assignmentRepository;


	@Override
	public void authorise() {
		boolean status = true;

		if (super.getRequest().hasData("id")) {
			boolean futureLeg = true;
			boolean legPublished = true;
			Integer legId = super.getRequest().getData("leg", int.class);
			if (legId != 0) {
				Leg leg = this.assignmentRepository.findLegById(legId);
				futureLeg = leg != null && !MomentHelper.isPast(leg.getScheduledArrival());
				legPublished = leg != null && !leg.isDraftMode();
			}

			FlightCrewMember crew = (FlightCrewMember) super.getRequest().getPrincipal().getActiveRealm();
			status = futureLeg && legPublished && crew != null;
		}
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		FlightAssignment assignment = new FlightAssignment();
		assignment.setLastUpdate(MomentHelper.getCurrentMoment());
		assignment.setDraftMode(true);

		FlightCrewMember currentUser = (FlightCrewMember) super.getRequest().getPrincipal().getActiveRealm();
		assignment.setCrewMember(currentUser);

		super.getBuffer().addData(assignment);
	}

	@Override
	public void validate(final FlightAssignment assignment) {
		boolean confirmation = super.getRequest().getData("confirmation", boolean.class);
		super.state(confirmation, "confirmation", "acme.validation.confirmation.message");
	}

	@Override
	public void bind(final FlightAssignment assignment) {
		Integer legId = super.getRequest().getData("leg", int.class);
		Leg leg = this.assignmentRepository.findLegById(legId);

		super.bindObject(assignment, "duty", "status", "remarks");
		assignment.setLeg(leg);
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

		Dataset data = super.unbindObject(assignment, "status", "remarks");
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
		data.put("draftMode", true);

		super.getResponse().addData(data);
	}
}
