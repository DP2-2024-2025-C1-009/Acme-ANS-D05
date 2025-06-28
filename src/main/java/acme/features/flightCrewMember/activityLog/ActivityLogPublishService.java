
package acme.features.flightCrewMember.activityLog;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.activityLog.ActivityLog;
import acme.realms.flightCrewMembers.FlightCrewMember;

@GuiService
public class ActivityLogPublishService extends AbstractGuiService<FlightCrewMember, ActivityLog> {

	@Autowired
	private ActivityLogRepository activityLogRepository;


	@Override
	public void authorise() {
		int id = super.getRequest().getData("id", int.class);
		ActivityLog log = this.activityLogRepository.findActivityLogById(id);

		boolean isCrew = log != null && log.getActivityLogAssignment().getCrewMember().getId() == super.getRequest().getPrincipal().getActiveRealm().getId();

		boolean assignmentPublished = log != null && !log.getActivityLogAssignment().getDraftMode();

		super.getResponse().setAuthorised(isCrew && assignmentPublished);
	}

	@Override
	public void load() {
		int id = super.getRequest().getData("id", int.class);
		ActivityLog log = this.activityLogRepository.findActivityLogById(id);
		super.getBuffer().addData(log);
	}

	@Override
	public void bind(final ActivityLog log) {
		super.bindObject(log, "incidentType", "description", "severityLevel");
	}

	@Override
	public void validate(final ActivityLog log) {

	}

	@Override
	public void perform(final ActivityLog log) {
		log.setDraftMode(false);
		this.activityLogRepository.save(log);
	}

	@Override
	public void unbind(final ActivityLog log) {
		Dataset data = super.unbindObject(log, "registrationMoment", "incidentType", "description", "severityLevel", "draftMode");

		data.put("readonly", false);
		data.put("moment", log.getRegistrationMoment());

		data.put("incidentType", log.getIncidentType() != null ? log.getIncidentType() : "");
		data.put("description", log.getDescription() != null ? log.getDescription() : "");
		data.put("severityLevel", log.getSeverityLevel() != null ? log.getSeverityLevel() : "");

		data.put("assignmentId", log.getActivityLogAssignment().getId());
		data.put("draftMode", log.getDraftMode());
		data.put("draftModeFlightAssignment", log.getActivityLogAssignment().getDraftMode());

		data.put("showAction", true);

		super.getResponse().addData(data);
	}

}
