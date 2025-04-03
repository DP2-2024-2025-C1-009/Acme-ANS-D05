
package acme.features.flightCrewMember.activityLog;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.activityLog.ActivityLog;
import acme.realms.flightCrewMembers.FlightCrewMember;

@GuiService
public class ActivityLogShowService extends AbstractGuiService<FlightCrewMember, ActivityLog> {

	@Autowired
	private ActivityLogRepository ActivityLogRepository;


	@Override
	public void authorise() {
		boolean isAuthorised = super.getRequest().getPrincipal().hasRealmOfType(FlightCrewMember.class);
		super.getResponse().setAuthorised(isAuthorised);
	}

	@Override
	public void load() {
		int recordId = super.getRequest().getData("id", int.class);
		ActivityLog log = this.ActivityLogRepository.findActivityLogById(recordId);
		super.getBuffer().addData(log);
	}

	@Override
	public void unbind(final ActivityLog log) {
		Dataset data = super.unbindObject(log, "registrationMoment", "incidentType", "description", "severityLevel", "draftMode");

		boolean isAssignmentInDraft = this.ActivityLogRepository.findFlightAssignmentById(log.getActivityLogAssignment().getId()).getDraftMode();

		data.put("draftModeFlightAssignment", isAssignmentInDraft);

		super.getResponse().addData(data);
	}
}
