
package acme.features.assistanceAgent.trackingLog;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.datatypes.ClaimStatus;
import acme.entities.agents.TrackingLog;
import acme.realms.AssistanceAgent;

@GuiService
public class AssistanceAgentTrackingLogUpdateService extends AbstractGuiService<AssistanceAgent, TrackingLog> {

	@Autowired
	private AssistanceAgentTrackingLogRepository repository;


	@Override
	public void authorise() {
		boolean authorised;

		int trackingLogId;
		String trackingLogIdRaw;
		TrackingLog trackingLog;

		trackingLog = null;

		if (super.getRequest().hasData("id")) {
			trackingLogIdRaw = super.getRequest().getData("id", String.class);

			try {
				trackingLogId = Integer.parseInt(trackingLogIdRaw);
			} catch (NumberFormatException e) {
				trackingLogId = -1;
			}
			trackingLog = this.repository.findTrackingLogById(trackingLogId);
		}
		authorised = trackingLog != null && !trackingLog.getIsPublished() && trackingLog.getTrackingSteps() != null && trackingLog.getTrackingSteps().getRegisteredBy() != null
			&& super.getRequest().getPrincipal().hasRealm(trackingLog.getTrackingSteps().getRegisteredBy());
		super.getResponse().setAuthorised(authorised);
	}

	@Override
	public void load() {
		int id;
		TrackingLog trackingLog;

		id = super.getRequest().getData("id", int.class);
		trackingLog = this.repository.findTrackingLogById(id);

		super.getBuffer().addData(trackingLog);
	}

	@Override
	public void bind(final TrackingLog trackingLog) {
		super.bindObject(trackingLog, "steps", "resolutionPercentage", "resolution", "status");
	}

	@Override
	public void validate(final TrackingLog trackingLog) {

	}

	@Override
	public void perform(final TrackingLog trackingLog) {
		trackingLog.setUpdateMoment(MomentHelper.getCurrentMoment());
		this.repository.save(trackingLog);
	}

	@Override
	public void unbind(final TrackingLog trackingLog) {
		Dataset dataset;
		SelectChoices statusChoices;

		statusChoices = SelectChoices.from(ClaimStatus.class, trackingLog.getStatus());

		dataset = super.unbindObject(trackingLog, "updateMoment", "creationMoment", "step", "resolutionPercentage", "resolution", "isPublished");
		dataset.put("statuses", statusChoices);
		dataset.put("status", statusChoices.getSelected().getKey());

		if (trackingLog.getTrackingSteps() != null)
			super.getResponse().addGlobal("isClaimPublished", trackingLog.getTrackingSteps().getCIsAccepted());
		else
			super.getResponse().addGlobal("isClaimPublished", false);
		super.getResponse().addData(dataset);
	}
}
