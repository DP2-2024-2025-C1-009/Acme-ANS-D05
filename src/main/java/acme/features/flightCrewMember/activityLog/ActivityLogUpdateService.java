
package acme.features.flightCrewMember.activityLog;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
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
		boolean isFlightCrew = super.getRequest().getPrincipal().hasRealmOfType(FlightCrewMember.class);
		super.getResponse().setAuthorised(isFlightCrew);
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
		if (!log.getDraftMode())
			super.state(false, "*", "activity-log.error.already-published");
	}

	@Override
	public void perform(final ActivityLog log) {
		this.ActivityLogRepository.save(log);
	}

	@Override
	public void unbind(final ActivityLog log) {
		Dataset data = super.unbindObject(log, "registrationMoment", "incidentType", "description", "severityLevel", "draftMode");
		super.getResponse().addData(data);
	}
}
