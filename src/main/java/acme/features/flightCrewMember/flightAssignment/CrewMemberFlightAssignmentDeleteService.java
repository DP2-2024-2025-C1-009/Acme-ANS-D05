
package acme.features.flightCrewMember.flightAssignment;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.activityLog.ActivityLog;
import acme.entities.flightAssignment.AssignmentStatus;
import acme.entities.flightAssignment.Duty;
import acme.entities.flightAssignment.FlightAssignment;
import acme.entities.legs.Leg;
import acme.realms.flightCrewMembers.FlightCrewMember;

@GuiService
public class CrewMemberFlightAssignmentDeleteService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

	@Autowired
	private CrewMemberFlightAssignmentRepository assignmentRepository;


	@Override
	public void authorise() {
		int id = super.getRequest().getData("id", int.class);
		FlightAssignment assignment = this.assignmentRepository.findById(id);
		boolean canDelete = assignment != null && assignment.getDraftMode() && super.getRequest().getPrincipal().hasRealm(assignment.getCrewMember());

		super.getResponse().setAuthorised(canDelete);
	}

	@Override
	public void load() {
		int id = super.getRequest().getData("id", int.class);
		FlightAssignment assignment = this.assignmentRepository.findById(id);
		super.getBuffer().addData(assignment);
	}

	@Override
	public void bind(final FlightAssignment assignment) {
		Integer legId = super.getRequest().getData("leg", int.class);
		Leg leg = this.assignmentRepository.findLegById(legId);

		super.bindObject(assignment, "duty", "lastUpdate", "status", "remarks");
		assignment.setLeg(leg);

		FlightCrewMember crew = (FlightCrewMember) super.getRequest().getPrincipal().getActiveRealm();
		assignment.setCrewMember(crew);
	}

	@Override
	public void perform(final FlightAssignment assignment) {
		Collection<ActivityLog> logs = this.assignmentRepository.findRelatedLogs(assignment.getId());
		this.assignmentRepository.deleteAll(logs);
		this.assignmentRepository.delete(assignment);
	}

	@Override
	public void validate(final FlightAssignment assignment) {
	}

	@Override
	public void unbind(final FlightAssignment assignment) {
		Dataset data;

		SelectChoices dutyChoices = SelectChoices.from(Duty.class, assignment.getDuty());
		SelectChoices statusChoices = SelectChoices.from(AssignmentStatus.class, assignment.getStatus());

		Collection<Leg> legs = this.assignmentRepository.findAllLegs(); // O usa findLegsByAirline(...) si tienes filtro por aerolínea
		SelectChoices legChoices = SelectChoices.from(legs, "flightNumber", assignment.getLeg());

		data = super.unbindObject(assignment, "duty", "lastUpdate", "status", "remarks", "draftMode");

		data.put("confirmation", false);
		data.put("readonly", false);
		data.put("moment", assignment.getLastUpdate());
		data.put("dutyChoices", dutyChoices);
		data.put("duty", dutyChoices.getSelected().getKey());
		data.put("statusChoices", statusChoices);
		data.put("status", statusChoices.getSelected().getKey());
		data.put("leg", legChoices.getSelected().getKey());
		data.put("legChoices", legChoices);
		data.put("crewMember", assignment.getCrewMember().getIdentity().getFullName());

		super.getResponse().addData(data);
	}
}
