
package acme.features.flightCrewMember.activityLog;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.helpers.MomentHelper;
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

		boolean correctCrew = log != null && log.getActivityLogAssignment().getCrewMember().getId() == super.getRequest().getPrincipal().getActiveRealm().getId();

		boolean assignmentPublished = log != null && !log.getActivityLogAssignment().getDraftMode();
		boolean inPast = log != null && MomentHelper.isPast(log.getActivityLogAssignment().getLeg().getScheduledArrival());
		boolean isDraft = log != null && log.getDraftMode();

		super.getResponse().setAuthorised(correctCrew && assignmentPublished && inPast && isDraft);
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

		data.put("registrationMoment", log.getRegistrationMoment());
		data.put("assignmentId", log.getActivityLogAssignment().getId());
		data.put("draftMode", log.getActivityLogAssignment().getDraftMode());

		super.getResponse().addData(data);
	}

}
