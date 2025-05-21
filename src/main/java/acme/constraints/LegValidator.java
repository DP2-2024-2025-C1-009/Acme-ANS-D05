
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.legs.Leg;
import acme.entities.legs.LegRepository;

@Validator
public class LegValidator extends AbstractValidator<ValidLeg, Leg> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private LegRepository repository;

	// ConstraintValidator interface ------------------------------------------


	@Override
	protected void initialise(final ValidLeg annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Leg leg, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;

		if (leg == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {
			// Validación: flightNumber único
			boolean uniqueLeg = true;
			Leg existingLeg = leg.getFlightNumber() != null ? this.repository.findLegByFlightNumber(leg.getFlightNumber()) : null;
			uniqueLeg = existingLeg == null || existingLeg.equals(leg);
			super.state(context, uniqueLeg, "flightNumber", "acme.validation.leg.duplicated-flightNumber");

			// Validación: los primeros 3 caracteres del flightNumber coinciden con el IATA
			boolean correctFlightNumber = true;

			if (leg.getFlightNumber() != null && leg.getFlight() != null && leg.getFlight().getManager() != null && leg.getFlight().getManager().getAirline() != null && leg.getFlight().getManager().getAirline().getIataCode() != null) {

				String flightNumber = leg.getFlightNumber();
				String iataCode = leg.getFlight().getManager().getAirline().getIataCode();

				if (flightNumber.length() >= 3 && iataCode.length() >= 3)
					correctFlightNumber = flightNumber.substring(0, 3).equals(iataCode.substring(0, 3));
				else
					correctFlightNumber = false;
			} else
				correctFlightNumber = false;

			super.state(context, correctFlightNumber, "flightNumber", "acme.validation.leg.flightNumberIATA");

			// Validación: scheduledArrival posterior a scheduledDeparture
			boolean arrivalAfterDeparture = leg.getScheduledArrival() != null && leg.getScheduledDeparture() != null && leg.getScheduledArrival().after(leg.getScheduledDeparture());

			super.state(context, arrivalAfterDeparture, "scheduledArrival", "acme.validation.leg.scheduledArrival-departure");

			// Validación: departureAirport y arrivalAirport distintos
			boolean differentAirport = leg.getDepartureAirport() != null && leg.getArrivalAirport() != null && !leg.getDepartureAirport().equals(leg.getArrivalAirport());

			super.state(context, differentAirport, "arrivalAirport", "acme.validation.leg.different-airport");
		}

		result = !super.hasErrors(context);
		return result;
	}
}
