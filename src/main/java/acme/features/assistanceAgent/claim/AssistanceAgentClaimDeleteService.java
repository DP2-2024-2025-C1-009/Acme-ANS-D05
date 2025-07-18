
package acme.features.assistanceAgent.claim;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.datatypes.ClaimStatus;
import acme.datatypes.ClaimType;
import acme.entities.agents.Claim;
import acme.entities.agents.TrackingLog;
import acme.entities.legs.Leg;
import acme.realms.AssistanceAgent;

@GuiService
public class AssistanceAgentClaimDeleteService extends AbstractGuiService<AssistanceAgent, Claim> {

	@Autowired
	private AssistanceAgentClaimRepository repository;


	@Override
	public void authorise() {
		boolean authorised;

		int claimId, legId;
		String claimIdRaw, legIdRaw;
		Claim claim;
		Leg leg;

		claim = null;
		leg = null;

		authorised = true;

		if (super.getRequest().hasData("id")) {
			claimIdRaw = super.getRequest().getData("id", String.class);

			try {
				claimId = Integer.parseInt(claimIdRaw);
			} catch (NumberFormatException e) {
				claimId = -1;
			}
			claim = this.repository.findClaimById(claimId);
			authorised = claim != null && !claim.getCIsAccepted() && claim.getRegisteredBy() != null && super.getRequest().getPrincipal().hasRealm(claim.getRegisteredBy());
		} else
			authorised = false;

		if (super.getRequest().hasData("leg")) {
			legIdRaw = super.getRequest().getData("leg", String.class);

			try {
				legId = Integer.parseInt(legIdRaw);
			} catch (NumberFormatException e) {
				legId = -1;
			}

			if (legId != 0) {
				leg = this.repository.findLegById(legId);
				authorised &= leg != null && !leg.isDraftMode() && claim != null && MomentHelper.isAfterOrEqual(claim.getRegistrationMoment(), leg.getScheduledArrival());
			}
		}
		super.getResponse().setAuthorised(authorised);
	}

	@Override
	public void load() {
		int claimId;
		Claim claim;

		claimId = super.getRequest().getData("id", int.class);
		claim = this.repository.findClaimById(claimId);

		super.getBuffer().addData(claim);
	}

	@Override
	public void bind(final Claim claim) {
		int legId;
		Leg leg;

		legId = super.getRequest().getData("leg", int.class);
		leg = this.repository.findLegById(legId);

		super.bindObject(claim, "passengerEmail", "description", "type");
		claim.setLeg(leg);
	}

	@Override
	public void validate(final Claim claim) {

	}

	@Override
	public void perform(final Claim claim) {
		Collection<TrackingLog> trackingLogs;

		trackingLogs = this.repository.findAllTrackingLogsByClaimId(claim.getId());

		this.repository.deleteAll(trackingLogs);
		this.repository.delete(claim);
	}

	@Override
	public void unbind(final Claim claim) {
		Dataset dataset;
		SelectChoices typeChoices, statusChoices, legChoices;
		Collection<Leg> legs;

		legs = this.repository.findAllPublishedLegsBefore(claim.getRegistrationMoment());

		typeChoices = SelectChoices.from(ClaimType.class, claim.getType());
		statusChoices = SelectChoices.from(ClaimStatus.class, claim.getStatus());
		legChoices = SelectChoices.from(legs, "flightNumber", claim.getLeg());

		dataset = super.unbindObject(claim, "registrationMoment", "passengerEmail", "description", "cIsAccepted");
		dataset.put("types", typeChoices);
		dataset.put("type", typeChoices.getSelected().getKey());
		dataset.put("statuses", statusChoices);
		dataset.put("status", statusChoices.getSelected().getKey());
		dataset.put("legs", legChoices);
		dataset.put("leg", legChoices.getSelected().getKey());

		super.getResponse().addData(dataset);
	}
}
