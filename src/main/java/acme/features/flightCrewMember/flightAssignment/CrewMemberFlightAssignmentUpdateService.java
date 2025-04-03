
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

@GuiService
public class CrewMemberFlightAssignmentUpdateService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

	@Autowired
	private CrewMemberFlightAssignmentRepository assignmentRepository;


	@Override
	public void authorise() {
		int id = super.getRequest().getData("id", int.class);
		FlightAssignment assignment = this.assignmentRepository.findById(id);
		boolean canEdit = assignment != null && assignment.getDraftMode() && super.getRequest().getPrincipal().hasRealm(assignment.getCrewMember());

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
		super.bindObject(assignment, "duty", "status", "remarks", "leg");
	}

	@Override
	public void validate(final FlightAssignment assignment) {
		super.state(assignment.getDraftMode(), "*", "acme.validation.flightAssignment.published.cannot-edit");
		super.state(assignment.getDuty() == Duty.LEAD_ATTENDANT, "duty", "acme.validation.flightAssignment.duty.lead-attendant");
	}

	@Override
	public void perform(final FlightAssignment assignment) {
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
