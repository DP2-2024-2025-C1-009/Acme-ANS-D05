
package acme.constraints;

import java.util.Comparator;
import java.util.Date;
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
			boolean hasLegs;

			List<Leg> flightLegs = this.repository.findLegsByFlight(flight.getId());

			hasLegs = flight.isDraftMode() ? true : !flightLegs.isEmpty();

			super.state(context, hasLegs, "tag", "acme.validation.flight.no-legs.message");
		}
		{
			boolean publishedLegs;

			List<Leg> flightLegs = this.repository.findDraftLegsByFlight(flight.getId());

			publishedLegs = flight.isDraftMode() ? true : flightLegs.isEmpty();

			super.state(context, publishedLegs, "tag", "acme.validation.flight.unpublished-legs.message");
		}
		{
			boolean correctTimeOrder = true;
			List<Leg> legs = this.repository.findAllLegs(flight.getId());
			if (!legs.isEmpty()) {
				legs.sort(Comparator.comparing(Leg::getScheduledDeparture));
				Date actualTime = legs.get(0).getScheduledArrival();
				legs.remove(0);
				for (Leg leg : legs)
					if (!leg.getScheduledDeparture().after(actualTime)) {
						correctTimeOrder = false;
						break;
					} else
						actualTime = leg.getScheduledArrival();
			}
			super.state(context, correctTimeOrder, "*", "acme.validation.flight.correctTimeOrder");
		}

		result = !super.hasErrors(context);

		return result;
	}

}
