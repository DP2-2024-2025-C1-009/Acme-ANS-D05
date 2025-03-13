
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;

@Validator
public class IATACodeValidator extends AbstractValidator<ValidIATACode, String> {

	@Override
	protected void initialise(final ValidIATACode annotation) {
		// No initialization required
	}

	@Override
	public boolean isValid(final String iataCode, final ConstraintValidatorContext context) {
		assert context != null;
		boolean result;
		boolean isNull = iataCode == null;

		if (!isNull) {
			boolean valid = iataCode.length() == 3 && iataCode.charAt(2) == 'X';
			super.state(context, valid, "iataCode", "acme.validation.airline.iata-code-last-letter");
		}

		result = !super.hasErrors(context);
		return result;
	}
}
