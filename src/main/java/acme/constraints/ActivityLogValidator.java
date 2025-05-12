
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.MomentHelper;
import acme.entities.activityLog.ActivityLog;

@Validator
public class ActivityLogValidator extends AbstractValidator<ValidActivityLog, ActivityLog> {

	@Override
	protected void initialise(final ValidActivityLog annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final ActivityLog activityLog, final ConstraintValidatorContext context) {
		if (activityLog == null)
			return false;

		final boolean fechaCorrecta = activityLog.getRegistrationMoment() != null && activityLog.getActivityLogAssignment() != null && activityLog.getActivityLogAssignment().getLeg() != null
			&& activityLog.getActivityLogAssignment().getLeg().getScheduledArrival() != null && MomentHelper.isAfter(activityLog.getRegistrationMoment(), activityLog.getActivityLogAssignment().getLeg().getScheduledArrival());

		super.state(context, fechaCorrecta, "registrationMoment", "{acme.validation.activityLog.registrationMoment.afterArrival}");

		return !super.hasErrors(context);
	}

}
