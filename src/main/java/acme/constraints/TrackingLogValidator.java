
package acme.constraints;

import java.util.Comparator;
import java.util.List;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.MomentHelper;
import acme.client.helpers.StringHelper;
import acme.datatypes.ClaimStatus;
import acme.entities.agents.TrackingLog;
import acme.entities.agents.TrackingLogRepository;

@Validator
public class TrackingLogValidator extends AbstractValidator<ValidTrackingLog, TrackingLog> {

	@Autowired
	private TrackingLogRepository repository;


	@Override
	protected void initialise(final ValidTrackingLog annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final TrackingLog trackingLog, final ConstraintValidatorContext context) {
		boolean result;

		assert context != null;

		if (trackingLog == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else if (trackingLog.getStatus() != null && trackingLog.getTrackingSteps() != null && trackingLog.getResolutionPercentage() != null && trackingLog.getIsPublished() != null && trackingLog.getUpdateMoment() != null
			&& trackingLog.getCreationMoment() != null) {
			{
				super.state(context, MomentHelper.isAfterOrEqual(trackingLog.getCreationMoment(), trackingLog.getTrackingSteps().getRegistrationMoment()), "creationMoment", "acme.validation.trackingLog.creationMoment.message");
				super.state(context, MomentHelper.isAfterOrEqual(trackingLog.getUpdateMoment(), trackingLog.getCreationMoment()), "updateMoment", "acme.validation.trackingLog.updateMoment.message");
			}
			{
				if (trackingLog.getResolutionPercentage() == 100.0)
					super.state(context, !trackingLog.getStatus().equals(ClaimStatus.PENDING), "status", "acme.validation.trackingLog.notPendingStatus.message");
				else
					super.state(context, trackingLog.getStatus().equals(ClaimStatus.PENDING), "status", "acme.validation.trackingLog.pendingStatus.message");
			}
			{
				if (!trackingLog.getStatus().equals(ClaimStatus.PENDING))
					super.state(context, !StringHelper.isBlank(trackingLog.getResolution()), "resolution", "acme.validation.trackingLog.resolution.message");
			}
			{
				super.state(context, !(trackingLog.getIsPublished() && !trackingLog.getTrackingSteps().getCIsAccepted()), "isPublished", "acme.validation.trackingLog.isPublished.message");
			}
			{
				List<TrackingLog> trackingLogs;
				Long completedTrackingLogs;

				trackingLogs = this.repository.findAllByClaimId(trackingLog.getTrackingSteps().getId());

				if (trackingLogs.contains(trackingLog)) {
					int index;

					index = trackingLogs.indexOf(trackingLog);
					trackingLogs.set(index, trackingLog);
				} else
					trackingLogs.add(trackingLog);
				trackingLogs.sort(Comparator.comparing(t -> t.getCreationMoment()));

				completedTrackingLogs = trackingLogs.stream().filter(t -> t.getResolutionPercentage() == 100.0).count();
				super.state(context, completedTrackingLogs <= 2, "status", "acme.validation.trackingLog.completedNumber.message");

				for (Integer i = 0; i < trackingLogs.size(); i++) {
					TrackingLog t1;
					TrackingLog t2;

					t1 = trackingLogs.get(i);

					if (trackingLogs.size() > i + 1) {
						t2 = trackingLogs.get(i + 1);

						if (t1.getResolutionPercentage() == 100.0 && t2.getResolutionPercentage() == 100.0) {
							super.state(context, t1.getIsPublished(), "status", "acme.validation.trackingLog.reclaimed.noCompletedLog.message");
							super.state(context, t1.getStatus().equals(t2.getStatus()), "status", "acme.validation.trackingLog.reclaimed.status.message");
						} else
							super.state(context, t1.getResolutionPercentage() < t2.getResolutionPercentage(), "resolutionPercentage", "acme.validation.trackingLog.resolutionPercentage.message");
					}
				}
			}
		}
		result = !super.hasErrors(context);
		return result;
	}
}
