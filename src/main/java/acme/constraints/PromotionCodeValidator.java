
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.MomentHelper;
import acme.client.helpers.StringHelper;

@Validator
public class PromotionCodeValidator extends AbstractValidator<ValidPromotionCode, String> {

	@Override
	protected void initialise(final ValidPromotionCode annotation) {
		// No initialization required
	}

	@Override
	public boolean isValid(final String promotionCode, final ConstraintValidatorContext context) {
		assert context != null;

		// Si el código está en blanco (null o vacío), se considera válido
		if (StringHelper.isBlank(promotionCode))
			return true;

		boolean result;
		int dashIndex = promotionCode.indexOf("-");
		if (dashIndex < 0 || dashIndex >= promotionCode.length() - 1)
			super.state(context, false, "promotionCode", "acme.validation.service.promotion-code-year");
		else {
			String codeYearDigits = promotionCode.substring(dashIndex + 1);
			int currentYear = MomentHelper.getCurrentMoment().getYear();
			String expectedDigits = String.format("%02d", currentYear % 100);
			boolean valid = codeYearDigits.equals(expectedDigits);
			super.state(context, valid, "promotionCode", "acme.validation.service.promotion-code-year");
		}

		result = !super.hasErrors(context);
		return result;
	}

}
