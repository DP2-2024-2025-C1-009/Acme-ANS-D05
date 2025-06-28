
package acme.features.flightCrewMember.activityLog;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.activityLog.ActivityLog;
import acme.realms.flightCrewMembers.FlightCrewMember;

@GuiService
public class ActivityLogDeleteService extends AbstractGuiService<FlightCrewMember, ActivityLog> {

	@Autowired
	private ActivityLogRepository ActivityLogRepository;


	@Override
	public void authorise() {
		int id = super.getRequest().getData("id", int.class);
		ActivityLog log = this.ActivityLogRepository.findActivityLogById(id);

		boolean correctCrew = log != null && log.getActivityLogAssignment().getCrewMember().getId() == super.getRequest().getPrincipal().getActiveRealm().getId();

		boolean isDraft = log != null && log.getDraftMode();

		super.getResponse().setAuthorised(correctCrew && isDraft);
	}

	@Override
	public void load() {
		int id = super.getRequest().getData("id", int.class);
		ActivityLog log = this.ActivityLogRepository.findActivityLogById(id);
		super.getBuffer().addData(log);
	}

	@Override
	public void bind(final ActivityLog log) {

	}

	@Override
	public void validate(final ActivityLog log) {
	}

	@Override
	public void perform(final ActivityLog log) {
		this.ActivityLogRepository.delete(log);
	}

	@Override
	public void unbind(final ActivityLog log) {
		Dataset data = super.unbindObject(log, "registrationMoment", "incidentType", "description", "severityLevel", "draftMode");
		data.put("assignmentId", log.getActivityLogAssignment().getId());
		data.put("draftMode", log.getActivityLogAssignment().getDraftMode());
		super.getResponse().addData(data);
	}

}
