
package acme.features.flightCrewMember.activityLog;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.activityLog.ActivityLog;
import acme.realms.flightCrewMembers.FlightCrewMember;

@GuiService
public class ActivityLogUpdateService extends AbstractGuiService<FlightCrewMember, ActivityLog> {

	@Autowired
	private ActivityLogRepository ActivityLogRepository;


	@Override
	public void authorise() {
		int id = super.getRequest().getData("id", int.class);
		ActivityLog log = this.ActivityLogRepository.findActivityLogById(id);

		boolean correctCrew = log != null && //
			log.getActivityLogAssignment().getCrewMember().getId() == super.getRequest().getPrincipal().getActiveRealm().getId();

		boolean flightAssignmentPublished = log != null && !log.getActivityLogAssignment().getDraftMode();
		boolean legInPast = log != null && MomentHelper.isPast(log.getActivityLogAssignment().getLeg().getScheduledArrival());
		boolean draftMode = log != null && log.getDraftMode();

		boolean authorised = correctCrew && flightAssignmentPublished && legInPast && draftMode;
		super.getResponse().setAuthorised(authorised);
	}

	@Override
	public void load() {
		int id = super.getRequest().getData("id", int.class);
		ActivityLog log = this.ActivityLogRepository.findActivityLogById(id);
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
		this.ActivityLogRepository.save(log);
	}

	@Override
	public void unbind(final ActivityLog log) {
		Dataset data = super.unbindObject(log, "registrationMoment", "incidentType", "description", "severityLevel", "draftMode");
		data.put("registrationMoment", log.getRegistrationMoment());
		data.put("assignmentId", log.getActivityLogAssignment().getId());
		super.getResponse().addData(data);
	}
}
