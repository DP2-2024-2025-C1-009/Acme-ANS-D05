
package acme.features.flightCrewMember.activityLog;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.activityLog.ActivityLog;
import acme.entities.flightAssignment.FlightAssignment;
import acme.realms.flightCrewMembers.FlightCrewMember;

@GuiService
public class ActivityLogListService extends AbstractGuiService<FlightCrewMember, ActivityLog> {

	@Autowired
	private ActivityLogRepository ActivityLogRepository;


	@Override
	public void authorise() {
		int masterId = super.getRequest().getData("masterId", int.class);
		FlightAssignment assignment = this.ActivityLogRepository.findFlightAssignmentById(masterId);
		boolean authorised = assignment != null && (assignment.getCrewMember().getId() == super.getRequest().getPrincipal().getActiveRealm().getId() || !assignment.getDraftMode());
		super.getResponse().setAuthorised(authorised);
	}

	@Override
	public void load() {
		int masterId = super.getRequest().getData("masterId", int.class);
		Collection<ActivityLog> logs = this.ActivityLogRepository.findLogsByAssignmentId(masterId);
		super.getBuffer().addData(logs);
	}

	@Override
	public void unbind(final ActivityLog log) {
		Dataset data = super.unbindObject(log, "registrationMoment", "incidentType", "description", "severityLevel");
		super.addPayload(data, log, "registrationMoment", "incidentType");
		super.getResponse().addData(data);
	}

	@Override
	public void unbind(final Collection<ActivityLog> logs) {
		int masterId = super.getRequest().getData("masterId", int.class);
		FlightAssignment assignment = this.ActivityLogRepository.findFlightAssignmentById(masterId);
		boolean inPast = MomentHelper.isPast(assignment.getLeg().getScheduledArrival());
		boolean correctUser = super.getRequest().getPrincipal().getActiveRealm().getId() == assignment.getCrewMember().getId();
		boolean showCreate = inPast && correctUser;

		super.getResponse().addGlobal("masterId", masterId);
		super.getResponse().addGlobal("showCreate", showCreate);
	}
}
