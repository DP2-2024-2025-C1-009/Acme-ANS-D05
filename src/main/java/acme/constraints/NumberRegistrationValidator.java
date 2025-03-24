
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.aircraft.Aircraft;
import acme.entities.aircraft.AircraftRepository;

@Validator
public class NumberRegistrationValidator extends AbstractValidator<ValidNumberRegistration, Aircraft> {

	@Autowired
	private AircraftRepository repository;


	@Override
	public boolean isValid(final Aircraft aircraft, final ConstraintValidatorContext context) {
		if (aircraft == null || aircraft.getNumberRegistration() == null || aircraft.getNumberRegistration().isEmpty())
			return true;

		String numberRegistration = aircraft.getNumberRegistration();

		long count = this.repository.countByNumberRegistration(numberRegistration);

		if (count > 0) {
			super.state(context, false, "numberRegistration", "Register number must be unique");
			return false;
		}

		return true;
	}
}
