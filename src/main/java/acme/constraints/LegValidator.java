
package acme.constraints;

import java.util.Collection;

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

			// Validación: evitar solapamientos de Aircraft
			boolean aircraftAvailable = true;
			if (leg.getAircraft() != null && leg.getScheduledDeparture() != null && leg.getScheduledArrival() != null) {
				Collection<Leg> overlappingLegs = this.repository.findLegsByAircraftId(leg.getAircraft().getId());
				for (Leg other : overlappingLegs) {

					if (other.getId() == leg.getId())
						continue;

					boolean overlaps = !(leg.getScheduledArrival().before(other.getScheduledDeparture()) || leg.getScheduledDeparture().after(other.getScheduledArrival()));

					if (overlaps) {
						aircraftAvailable = false;
						break;
					}
				}
			}
			super.state(context, aircraftAvailable, "aircraft", "acme.validation.leg.aircraft-overlap");

			// Validación: evitar solapamientos entre Legs de un mismo vuelo

			boolean nonOverlappingWithSameFlight = true;
			if (leg.getFlight() != null && leg.getScheduledDeparture() != null && leg.getScheduledArrival() != null) {
				Collection<Leg> sameFlightLegs = this.repository.findLegsByFlightId(leg.getFlight().getId());
				for (Leg other : sameFlightLegs) {

					if (other.getId() == leg.getId())
						continue;

					boolean overlaps = !(leg.getScheduledArrival().before(other.getScheduledDeparture()) || leg.getScheduledDeparture().after(other.getScheduledArrival()));

					if (overlaps) {
						nonOverlappingWithSameFlight = false;
						break;
					}
				}
			}
			super.state(context, nonOverlappingWithSameFlight, "scheduledDeparture", "acme.validation.leg.overlap-same-flight");
		}

		result = !super.hasErrors(context);
		return result;
	}
}
