
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
import acme.realms.flightCrewMembers.FlightCrewMember;
import acme.realms.flightCrewMembers.FlightCrewMemberStatus;

@GuiService
public class CrewMemberFlightAssignmentPublishService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

	@Autowired
	protected CrewMemberFlightAssignmentRepository repository;


	@Override
	public void authorise() {
		int id = super.getRequest().getData("id", int.class);
		FlightAssignment assignment = this.repository.findOneAssignmentById(id);

		boolean isOwner = assignment != null && super.getRequest().getPrincipal().hasRealm(assignment.getCrewMember());
		boolean isDraft = assignment != null && assignment.getDraftMode();

		super.getResponse().setAuthorised(isOwner && isDraft);
	}

	@Override
	public void load() {
		int id = super.getRequest().getData("id", int.class);
		FlightAssignment assignment = this.repository.findOneAssignmentById(id);
		super.getBuffer().addData(assignment);
	}

	@Override
	public void bind(final FlightAssignment object) {
		assert object != null;
		super.bindObject(object, "remarks");
	}

	@Override
	public void validate(final FlightAssignment object) {
		assert object != null;

		boolean isLeadAttendant = object.getDuty() == Duty.LEAD_ATTENDANT;
		super.state(isLeadAttendant, "duty", "acme.validation.flight-assignment.duty.lead-attendant-only");

		boolean legNotOccurred = object.getLeg().getScheduledDeparture().after(MomentHelper.getCurrentMoment());
		super.state(legNotOccurred, "leg", "acme.validation.flight-assignment.leg.already-started");

		boolean isAvailable = object.getCrewMember().getFlightCrewMemberStatus() == FlightCrewMemberStatus.AVAILABLE;
		super.state(isAvailable, "crewMember", "acme.validation.flight-assignment.crew-member.not-available");

		boolean notAssignedElsewhere = this.repository.findAssignmentsByCrewMemberId(object.getCrewMember().getId()).stream().filter(fa -> fa.getId() != object.getId()) // evitar la misma
			.noneMatch(fa -> fa.getLeg().equals(object.getLeg()));
		super.state(notAssignedElsewhere, "*", "acme.validation.flight-assignment.crew-member.already-assigned");

		// Validar único piloto y copiloto
		Collection<FlightAssignment> assignments = this.repository.findAssignmentsByLegId(object.getLeg().getId());

		long pilotCount = assignments.stream().filter(fa -> fa.getDuty() == Duty.PILOT && fa.getId() != object.getId()).count();
		long coPilotCount = assignments.stream().filter(fa -> fa.getDuty() == Duty.COPILOT && fa.getId() != object.getId()).count();

		if (object.getDuty() == Duty.PILOT)
			super.state(pilotCount < 1, "duty", "acme.validation.flight-assignment.duty.pilot-limit");

		if (object.getDuty() == Duty.COPILOT)
			super.state(coPilotCount < 1, "duty", "acme.validation.flight-assignment.duty.copilot-limit");
	}

	@Override
	public void perform(final FlightAssignment object) {
		object.setDraftMode(false);
		object.setLastUpdate(MomentHelper.getCurrentMoment());
		this.repository.save(object);
	}

	@Override
	public void unbind(final FlightAssignment object) {
		assert object != null;

		Dataset dataset = super.unbindObject(object, "duty", "status", "remarks", "draftMode", "lastUpdate");

		SelectChoices dutyChoices = SelectChoices.from(Duty.class, object.getDuty());
		SelectChoices statusChoices = SelectChoices.from(AssignmentStatus.class, object.getStatus());

		dataset.put("duty", dutyChoices.getSelected().getKey());
		dataset.put("dutyChoices", dutyChoices);

		dataset.put("status", statusChoices.getSelected().getKey());
		dataset.put("statusChoices", statusChoices);

		dataset.put("confirmation", false);

		super.getResponse().addData(dataset);
	}
}
