
package acme.features.flightCrewMember.activityLog;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.activityLog.ActivityLog;
import acme.entities.flightAssignment.FlightAssignment;
import acme.realms.flightCrewMembers.FlightCrewMember;

@GuiService
public class ActivityLogCreateService extends AbstractGuiService<FlightCrewMember, ActivityLog> {

	@Autowired
	private ActivityLogRepository ActivityLogRepository;


	@Override
	public void authorise() {
		int masterId = super.getRequest().getData("assignmentId", int.class);
		FlightAssignment assignment = this.ActivityLogRepository.findFlightAssignmentById(masterId);

		boolean isOwner = assignment != null && assignment.getCrewMember().getId() == super.getRequest().getPrincipal().getActiveRealm().getId();

		boolean legHasOccurred = assignment != null && MomentHelper.isPast(assignment.getLeg().getScheduledArrival());

		boolean status = isOwner && legHasOccurred;
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		ActivityLog log = new ActivityLog();

		log.setRegistrationMoment(MomentHelper.getCurrentMoment());

		int assignmentId = super.getRequest().getData("assignmentId", int.class);
		FlightAssignment assignment = this.ActivityLogRepository.findFlightAssignmentById(assignmentId);
		log.setActivityLogAssignment(assignment);

		log.setDraftMode(true);
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
		Dataset data = super.unbindObject(log, "incidentType", "description", "severityLevel", "draftMode");

		data.put("assignmentId", log.getActivityLogAssignment().getId());
		data.put("registrationMoment", log.getRegistrationMoment());

		super.getResponse().addData(data);
	}

}
