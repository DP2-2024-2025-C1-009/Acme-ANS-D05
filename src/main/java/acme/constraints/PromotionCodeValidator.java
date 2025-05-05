
package acme.constraints;

import java.util.Calendar;
import java.util.Date;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.MomentHelper;
import acme.entities.service.Service;

@Validator
public class PromotionCodeValidator extends AbstractValidator<ValidPromotionCode, Service> {

	@Override
	protected void initialise(final ValidPromotionCode annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Service service, final ConstraintValidatorContext context) {
		String promotionCode = service.getPromotionCode();
		if (promotionCode == null || promotionCode.equals(""))
			return true;

		if (!promotionCode.matches("^[A-Z]{4}-[0-9]{2}$")) {
			super.state(context, false, "*", "Pattern incorrect:" + promotionCode);
			return false;
		}

		String promotionCodeYear = promotionCode.substring(promotionCode.length() - 2);

		Date moment = MomentHelper.getCurrentMoment();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(moment);
		String actualCurrentYear = String.valueOf(calendar.get(Calendar.YEAR)).substring(2);

		if (!promotionCodeYear.equals(actualCurrentYear)) {
			super.state(context, false, "*", "Doesn't correspond to the actual year" + actualCurrentYear);
			return false;
		}

		return true;
	}

}
