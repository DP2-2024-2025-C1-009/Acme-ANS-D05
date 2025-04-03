
package acme.features.flightCrewMember.flightAssignment;

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
	private CrewMemberFlightAssignmentRepository assignmentRepository;


	@Override
	public void authorise() {
		int assignmentId = super.getRequest().getData("id", int.class);
		FlightAssignment assignment = this.assignmentRepository.findById(assignmentId);
		boolean isAuthorised = assignment != null && assignment.getDraftMode() && super.getRequest().getPrincipal().hasRealm(assignment.getCrewMember());

		super.getResponse().setAuthorised(isAuthorised);
	}

	@Override
	public void load() {
		int id = super.getRequest().getData("id", int.class);
		FlightAssignment assignment = this.assignmentRepository.findById(id);
		super.getBuffer().addData(assignment);
	}

	@Override
	public void bind(final FlightAssignment assignment) {
		super.bindObject(assignment, "duty", "status", "remarks", "leg");
	}

	@Override
	public void validate(final FlightAssignment assignment) {

		if (assignment.getDuty() != null && assignment.getLeg() != null) {
			boolean dutyTaken = this.assignmentRepository.isPilotOrCoPilotDuplicated(assignment.getLeg().getId(), assignment.getDuty(), assignment.getId());
			super.state(!dutyTaken, "duty", "acme.validation.flightAssignment.duty");
		}

		if (assignment.getLeg() != null) {
			boolean legIsPast = assignment.getLeg().getScheduledDeparture().before(MomentHelper.getCurrentMoment());
			super.state(!legIsPast, "leg", "acme.validation.flightAssignment.leg.moment");
		}

		if (assignment.getCrewMember() != null) {
			boolean isAvailable = assignment.getCrewMember().getFlightCrewMemberStatus().equals(FlightCrewMemberStatus.AVAILABLE);
			super.state(isAvailable, "crewMember", "acme.validation.flightAssignment.crewMember.available");

			boolean overlapping = this.assignmentRepository.isCrewMemberOverlapping(assignment.getCrewMember().getId(), MomentHelper.getCurrentMoment());
			super.state(!overlapping, "crewMember", "acme.validation.flightAssignment.crewMember.multipleLegs");
		}
	}

	@Override
	public void perform(final FlightAssignment assignment) {
		assignment.setDraftMode(false);
		this.assignmentRepository.save(assignment);
	}

	@Override
	public void unbind(final FlightAssignment assignment) {
		Dataset data = super.unbindObject(assignment, "duty", "lastUpdate", "status", "remarks", "draftMode", "crewMember", "leg");

		data.put("crewMember", assignment.getCrewMember().getIdentity().getFullName());

		SelectChoices dutyOptions = SelectChoices.from(Duty.class, assignment.getDuty());
		data.put("dutyChoices", dutyOptions);
		data.put("duty", dutyOptions.getSelected().getKey());

		SelectChoices statusOptions = SelectChoices.from(AssignmentStatus.class, assignment.getStatus());
		data.put("statusChoices", statusOptions);
		data.put("status", statusOptions.getSelected().getKey());

		SelectChoices legOptions = SelectChoices.from(this.assignmentRepository.findLegsByAirline(assignment.getCrewMember().getAirline().getId()), "flightNumber", assignment.getLeg());
		data.put("legChoices", legOptions);
		data.put("leg", legOptions.getSelected().getKey());

		super.getResponse().addData(data);
	}
}
