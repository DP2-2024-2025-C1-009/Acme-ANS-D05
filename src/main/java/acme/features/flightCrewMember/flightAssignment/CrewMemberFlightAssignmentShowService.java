
package acme.features.flightCrewMember.flightAssignment;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
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
	protected CrewMemberFlightAssignmentRepository repository;


	@Override
	public void authorise() {
		int id = super.getRequest().getData("id", int.class);
		FlightAssignment assignment = this.repository.findOneAssignmentById(id);
		boolean isOwner = assignment != null && super.getRequest().getPrincipal().hasRealm(assignment.getCrewMember());

		super.getResponse().setAuthorised(isOwner);
	}

	@Override
	public void load() {
		int id = super.getRequest().getData("id", int.class);
		FlightAssignment assignment = this.repository.findOneAssignmentById(id);
		super.getBuffer().addData(assignment);
	}

	@Override
	public void unbind(final FlightAssignment assignment) {
		assert assignment != null;

		Collection<Leg> legs = this.repository.findAllLegs();
		Collection<FlightCrewMember> crewMembers = this.repository.findCrewMembersByLegId(assignment.getLeg().getId());

		Dataset dataset = super.unbindObject(assignment, "duty", "status", "remarks", "draftMode", "lastUpdate");

		SelectChoices dutyChoices = SelectChoices.from(Duty.class, assignment.getDuty());
		SelectChoices statusChoices = SelectChoices.from(AssignmentStatus.class, assignment.getStatus());
		SelectChoices legChoices = SelectChoices.from(legs, "flightNumber", assignment.getLeg());

		dataset.put("duty", dutyChoices.getSelected().getKey());
		dataset.put("dutyChoices", dutyChoices);

		dataset.put("status", statusChoices.getSelected().getKey());
		dataset.put("statusChoices", statusChoices);

		dataset.put("leg", legChoices.getSelected().getKey());
		dataset.put("legs", legChoices);

		dataset.put("crewMembers", crewMembers);

		super.getResponse().addData(dataset);
	}
}
