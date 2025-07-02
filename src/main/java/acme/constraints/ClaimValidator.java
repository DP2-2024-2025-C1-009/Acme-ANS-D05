
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.MomentHelper;
import acme.datatypes.ClaimStatus;
import acme.entities.agents.Claim;

@Validator
public class ClaimValidator extends AbstractValidator<ValidClaim, Claim> {

	@Override
	protected void initialise(final ValidClaim annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Claim claim, final ConstraintValidatorContext context) {
		boolean result;

		assert context != null;

		if (claim == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else if (claim.getCIsAccepted() && claim.getStatus() != null && claim.getLeg() != null && claim.getRegistrationMoment() != null) {
			if (claim.getCIsAccepted() && claim.getStatus().equals(ClaimStatus.PENDING))
				super.state(context, false, "cIsAccepted", "acme.validation.claim.cIsAccepted.message");

			super.state(context, MomentHelper.isAfterOrEqual(claim.getRegistrationMoment(), claim.getLeg().getScheduledArrival()), "registrationMoment", "acme.validation.claim.registrationMoment.message");
			super.state(context, !claim.getLeg().isDraftMode(), "leg", "acme.validation.claim.leg.message");
		}
		result = !super.hasErrors(context);
		return result;
	}
}
