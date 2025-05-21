
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
			{

				// Verificamos que sea unico el flightNumber

				boolean uniqueLeg = true;
				Leg existingLeg;
				if (leg.getFlightNumber() != null)
					existingLeg = this.repository.findLegByFlightNumber(leg.getFlightNumber());
				else
					existingLeg = null;
				uniqueLeg = existingLeg == null || existingLeg.equals(leg);

				super.state(context, uniqueLeg, "flightNumber", "acme.validation.leg.duplicated-flightNumber");
			}
			{

				// Verificamos que coincida con el las letras del codigo IATA

				boolean correctFlightNumber = true;
				Leg existingLeg;
				if (leg.getFlightNumber() != null)
					existingLeg = this.repository.findLegByFlightNumber(leg.getFlightNumber());
				else
					existingLeg = null;
				for (int i = 0; i < 3; i++)
					if (correctFlightNumber == true)
						correctFlightNumber = existingLeg == null || existingLeg.getFlightNumber().charAt(i) == existingLeg.getFlight().getManager().getAirline().getIataCode().charAt(i);
					else
						i = 3;

				super.state(context, correctFlightNumber, "flightNumber", "acme.validation.leg.flightNumberIATA");
			}
			{

				// Verificamos que la hora de llegada sea posterior a la hora de salida

				boolean arrivalAfterDeparture;

				if (leg.getScheduledArrival() == null || leg.getScheduledDeparture() == null)
					arrivalAfterDeparture = false;
				else
					arrivalAfterDeparture = leg.getScheduledArrival().after(leg.getScheduledDeparture());

				super.state(context, arrivalAfterDeparture, "scheduledArrival", "acme.validation.leg.scheduledArrival-departure");
			}
			{

				// Verificamos que los aeropuertos de llegada y salida sean distintos

				boolean differentAirport;
				if (leg.getDepartureAirport() == null || leg.getArrivalAirport() == null)
					differentAirport = false;
				else
					differentAirport = leg.getDepartureAirport() != leg.getArrivalAirport();

				super.state(context, differentAirport, "arrivalAirport", "acme.validation.leg.different-airport");
			}
		}

		result = !super.hasErrors(context);

		return result;
	}

}
