
package acme.features.flightCrewMember.activityLog;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.activityLog.ActivityLog;
import acme.entities.flightAssignment.FlightAssignment;
import acme.realms.flightCrewMembers.FlightCrewMember;

@GuiService
public class ActivityLogPublishService extends AbstractGuiService<FlightCrewMember, ActivityLog> {

	@Autowired
	private ActivityLogRepository repository;


	@Override
	public void authorise() {
		boolean isFlightCrew = super.getRequest().getPrincipal().hasRealmOfType(FlightCrewMember.class);
		super.getResponse().setAuthorised(isFlightCrew);
	}

	@Override
	public void load() {
		int id = super.getRequest().getData("id", int.class);
		ActivityLog log = this.repository.findActivityLogById(id);
		super.getBuffer().addData(log);
	}

	@Override
	public void validate(final ActivityLog log) {
		FlightAssignment assignment = this.repository.findFlightAssignmentById(log.getActivityLogAssignment().getId());

		super.state(!log.getDraftMode(), "*", "activity-log.error.already-published");
		super.state(!assignment.getDraftMode(), "*", "activity-log.error.assignment-not-published");
	}

	@Override
	public void perform(final ActivityLog log) {
		log.setDraftMode(false);
		this.repository.save(log);
	}

}
