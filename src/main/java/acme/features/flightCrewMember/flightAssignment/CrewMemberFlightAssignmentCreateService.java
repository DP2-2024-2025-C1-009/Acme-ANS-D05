
package acme.features.authenticated.flightCrewMember.flightAssignment;

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
import acme.realms.flightCrewMembers.FlightCrewMemberStatus;

@GuiService
public class CrewMemberFlightAssignmentCreateService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

	@Autowired
	protected CrewMemberFlightAssignmentRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		FlightAssignment object = new FlightAssignment();
		object.setDraftMode(true);
		object.setLastUpdate(MomentHelper.getCurrentMoment());

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final FlightAssignment object) {
		assert object != null;

		int legId = super.getRequest().getData("leg", int.class);
		Leg leg = this.repository.findLegById(legId);

		int accountId = super.getRequest().getPrincipal().getAccountId();
		FlightCrewMember principal = this.repository.findFlightCrewMemberByAccountId(accountId);

		super.bindObject(object, "duty", "status", "remarks");
		object.setLeg(leg);
		object.setCrewMember(principal);
		object.setLastUpdate(MomentHelper.getCurrentMoment());
	}

	@Override
	public void validate(final FlightAssignment object) {
		assert object != null;

		boolean confirmation = super.getRequest().getData("confirmation", boolean.class);
		super.state(confirmation, "confirmation", "acme.validation.confirmation");

		boolean isLeadAttendant = object.getDuty().equals(Duty.LEAD_ATTENDANT);
		super.state(isLeadAttendant, "duty", "acme.validation.flight-assignment.duty.lead-attendant-only");

		boolean isAvailable = object.getCrewMember().getFlightCrewMemberStatus().equals(FlightCrewMemberStatus.AVAILABLE);
		super.state(isAvailable, "crewMember", "acme.validation.flight-assignment.crew-member.not-available");
	}

	@Override
	public void perform(final FlightAssignment object) {
		assert object != null;
		this.repository.save(object);
	}

	@Override
	public void unbind(final FlightAssignment object) {
		assert object != null;

		Collection<Leg> legs = this.repository.findAllLegs();

		Dataset dataset = super.unbindObject(object, "duty", "status", "remarks", "draftMode");

		SelectChoices dutyChoices = SelectChoices.from(Duty.class, object.getDuty());
		SelectChoices statusChoices = SelectChoices.from(AssignmentStatus.class, object.getStatus());
		SelectChoices legChoices = SelectChoices.from(legs, "flightNumber", object.getLeg());

		dataset.put("duty", dutyChoices.getSelected().getKey());
		dataset.put("dutyChoices", dutyChoices);

		dataset.put("status", statusChoices.getSelected().getKey());
		dataset.put("statusChoices", statusChoices);

		dataset.put("leg", legChoices.getSelected().getKey());
		dataset.put("legs", legChoices);

		dataset.put("confirmation", false);

		super.getResponse().addData(dataset);
	}
}
