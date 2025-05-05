
package acme.constraints;

import java.util.Optional;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.aircraft.Aircraft;
import acme.features.administrator.aircraft.AircraftRepository;

@Validator

public class NumberRegistrationValidator extends AbstractValidator<ValidNumberRegistration, Aircraft> {

	@Autowired
	private AircraftRepository repository;


	@Override
	protected void initialise(final ValidNumberRegistration annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Aircraft aircraft, final ConstraintValidatorContext context) {
		assert context != null;

		if (aircraft == null)
			return false;

		String numberRegistration = aircraft.getNumberRegistration();

		if (numberRegistration == null)
			return false;

		Optional<Aircraft> aircraftWithSameRegistrationNumber = this.repository.findAircraftByNumberRegistration(numberRegistration);
		if (aircraftWithSameRegistrationNumber.isPresent() && aircraftWithSameRegistrationNumber.get().getId() != aircraft.getId()) {
			super.state(context, false, "registrationNumber", "Registration number must be unique" + numberRegistration);
			return false;
		}

		return true;
	}

}
