
package acme.constraints;

import java.util.List;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.flight.Flight;
import acme.entities.flight.FlightRepository;
import acme.entities.legs.Leg;

@Validator
public class FlightValidator extends AbstractValidator<ValidFlight, Flight> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private FlightRepository repository;

	// ConstraintValidator interface ------------------------------------------


	@Override
	protected void initialise(final ValidFlight annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Flight flight, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;

		if (flight == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {
			boolean correctSelfTransfer;
			List<Leg> legs = this.repository.findAllLegs(flight.getId());
			if (flight.isDraftMode())
				correctSelfTransfer = true;
			else if (flight.isSelfTransfer())
				correctSelfTransfer = legs.size() > 1;
			else
				correctSelfTransfer = legs.size() == 1;

			super.state(context, correctSelfTransfer, "*", "acme.validation.flight.validSelfTransfer");

		}

		result = !super.hasErrors(context);

		return result;
	}

}
