
package acme.features.assistanceAgent.trackingLog;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.datatypes.ClaimStatus;
import acme.entities.agents.Claim;
import acme.entities.agents.TrackingLog;
import acme.realms.AssistanceAgent;

@GuiService
public class AssistanceAgentTrackingLogCreateService extends AbstractGuiService<AssistanceAgent, TrackingLog> {

	@Autowired
	private AssistanceAgentTrackingLogRepository repository;


	@Override
	public void authorise() {
		boolean authorised, canCreate;

		int masterId;
		String masterIdRaw;
		Claim claim;

		claim = null;
		canCreate = true;

		if (super.getRequest().hasData("masterId")) {
			masterIdRaw = super.getRequest().getData("masterId", String.class);

			try {
				masterId = Integer.parseInt(masterIdRaw);
			} catch (NumberFormatException e) {
				masterId = -1;
			}
			claim = this.repository.findClaimById(masterId);

			if (masterId >= 0) {
				Collection<TrackingLog> trackingLogs;

				trackingLogs = this.repository.findAllTrackingLogsByMasterId(masterId);
				canCreate = trackingLogs.stream().filter(t -> !t.getStatus().equals(ClaimStatus.PENDING)).count() < 2L;
			}
		}
		authorised = claim != null && claim.getRegisteredBy() != null && super.getRequest().getPrincipal().hasRealm(claim.getRegisteredBy()) && canCreate;
		super.getResponse().setAuthorised(authorised);
	}

	@Override
	public void load() {
		TrackingLog trackingLog;

		trackingLog = new TrackingLog();
		super.getBuffer().addData(trackingLog);
	}

	@Override
	public void bind(final TrackingLog trackingLog) {
		int masterId;
		Claim claim;

		masterId = super.getRequest().getData("masterId", int.class);
		claim = this.repository.findClaimById(masterId);

		super.bindObject(trackingLog, "steps", "resolutionPercentage", "resolution", "status");

		trackingLog.setIsPublished(false);
		trackingLog.setTrackingSteps(claim);
		trackingLog.setCreationMoment(MomentHelper.getCurrentMoment());
		trackingLog.setUpdateMoment(MomentHelper.getCurrentMoment());
	}

	@Override
	public void validate(final TrackingLog trackingLog) {

	}

	@Override
	public void perform(final TrackingLog trackingLog) {
		this.repository.save(trackingLog);
	}

	@Override
	public void unbind(final TrackingLog trackingLog) {
		int masterId;
		Dataset dataset;
		SelectChoices statusChoices;

		masterId = super.getRequest().getData("masterId", int.class);
		statusChoices = SelectChoices.from(ClaimStatus.class, trackingLog.getStatus());

		dataset = super.unbindObject(trackingLog, "updateMoment", "creationMoment", "steps", "resolutionPercentage", "resolution", "isPublished");
		dataset.put("statuses", statusChoices);
		dataset.put("status", statusChoices.getSelected().getKey());

		super.getResponse().addData(dataset);
		super.getResponse().addGlobal("masterId", masterId);
	}
}
