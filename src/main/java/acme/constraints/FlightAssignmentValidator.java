
package acme.constraints;

import java.util.List;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.MomentHelper;
import acme.entities.flightAssignment.Duty;
import acme.entities.flightAssignment.FlightAssignment;
import acme.entities.legs.Leg;
import acme.features.flightCrewMember.flightAssignment.CrewMemberFlightAssignmentRepository;
import acme.realms.flightCrewMembers.FlightCrewMemberStatus;

@Validator
public class FlightAssignmentValidator extends AbstractValidator<ValidFlightAssignment, FlightAssignment> {

	@Autowired
	private CrewMemberFlightAssignmentRepository repository;


	@Override
	protected void initialise(final ValidFlightAssignment annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final FlightAssignment assignment, final ConstraintValidatorContext context) {
		if (assignment == null)
			return false;

		if (assignment.getCrewMember() != null) {
			boolean available = assignment.getCrewMember().getFlightCrewMemberStatus() == FlightCrewMemberStatus.AVAILABLE;
			super.state(context, available, "crewMember", "acme.validation.flightAssignment.crewMember.available");
		}

		if (assignment.getLeg() != null && assignment.getCrewMember() != null) {
			List<FlightAssignment> otherAssignments = this.repository.findFlightAssignmentByCrewMemberId(assignment.getCrewMember().getId());

			for (FlightAssignment other : otherAssignments)
				if (!other.getLeg().isDraftMode() && !this.legIsCompatible(assignment.getLeg(), other.getLeg()) && other.getId() != assignment.getId()) {
					super.state(context, false, "leg", "acme.validation.flightAssignment.leg.overlap");
					break;
				}
		}

		if (assignment.getLeg() != null && assignment.getDuty() != null && !assignment.getLeg().isDraftMode()) {
			List<FlightAssignment> legAssignments = this.repository.findFlightAssignmentByLegId(assignment.getLeg().getId());

			boolean pilotAssigned = false;
			boolean copilotAssigned = false;

			for (FlightAssignment fa : legAssignments) {
				if (fa.getId() == assignment.getId())
					continue;

				if (fa.getDuty() == Duty.PILOT)
					pilotAssigned = true;
				if (fa.getDuty() == Duty.COPILOT)
					copilotAssigned = true;
			}

			if (assignment.getDuty() == Duty.PILOT)
				super.state(context, !pilotAssigned, "duty", "acme.validation.flightAssignment.duty.pilot.taken");

			if (assignment.getDuty() == Duty.COPILOT)
				super.state(context, !copilotAssigned, "duty", "acme.validation.flightAssignment.duty.copilot.taken");
		}

		return !super.hasErrors(context);
	}

	private boolean legIsCompatible(final Leg a, final Leg b) {
		boolean conflictStart = MomentHelper.isInRange(a.getScheduledDeparture(), b.getScheduledDeparture(), b.getScheduledArrival());
		boolean conflictEnd = MomentHelper.isInRange(a.getScheduledArrival(), b.getScheduledDeparture(), b.getScheduledArrival());
		return !conflictStart && !conflictEnd;
	}
}
