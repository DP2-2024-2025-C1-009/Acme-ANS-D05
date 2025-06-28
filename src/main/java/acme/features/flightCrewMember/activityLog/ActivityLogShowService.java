
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
		int id = super.getRequest().getData("id", int.class);
		ActivityLog log = this.ActivityLogRepository.findActivityLogById(id);

		boolean authorised = false;
		if (log != null) {
			var assignment = log.getActivityLogAssignment();
			int currentUserId = super.getRequest().getPrincipal().getActiveRealm().getId();
			boolean isOwner = assignment.getCrewMember().getId() == currentUserId;
			boolean isPublished = !assignment.getDraftMode();
			authorised = isOwner || isPublished;
		}

		super.getResponse().setAuthorised(authorised);
	}

	@Override
	public void load() {
		int recordId = super.getRequest().getData("id", int.class);
		ActivityLog log = this.ActivityLogRepository.findActivityLogById(recordId);
		super.getBuffer().addData(log);
	}

	@Override
	public void unbind(final ActivityLog log) {
		Dataset data;

		var assignment = log.getActivityLogAssignment();

		boolean correctUser = assignment.getCrewMember().getId() == super.getRequest().getPrincipal().getActiveRealm().getId();
		boolean showButtons = log.getDraftMode() && correctUser;
		boolean publishAvailable = correctUser && log.getDraftMode() && !assignment.getDraftMode();
		data = super.unbindObject(log, "registrationMoment", "incidentType", "description", "severityLevel", "draftMode");

		data.put("masterId", assignment.getId());
		data.put("buttonsAvailable", showButtons);
		data.put("publishAvailable", publishAvailable);
		data.put("draftModeFlightAssignment", assignment.getDraftMode());

		super.getResponse().addData(data);
	}
}
