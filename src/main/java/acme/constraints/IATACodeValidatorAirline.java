
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.airline.Airline;
import acme.features.administrator.airline.AirlineRepository;

@Validator
public class IATACodeValidatorAirline extends AbstractValidator<ValidAirline, Airline> {

	private static final String	IATA_REGEX	= "^[A-Z]{3}$";

	@Autowired
	private AirlineRepository	airlineRepository;


	@Override
	public boolean isValid(final Airline airline, final ConstraintValidatorContext context) {
		if (airline == null || airline.getIataCode() == null || airline.getIataCode().isEmpty()) {
			super.state(context, false, "iataCode", "IATA code cannot be empty");
			return false;
		}

		String iataCode = airline.getIataCode();

		if (!iataCode.matches(IATACodeValidatorAirline.IATA_REGEX)) {
			super.state(context, false, "iataCode", "acme.validation.airport.iata-code-pattern");
			return false;
		}

		int id = airline.getId();
		long count = this.airlineRepository.countByIataCodeExcludingId(iataCode, id);

		if (count > 0) {
			super.state(context, false, "iataCode", "acme.validation.airport.iata-code-pattern");
			return false;
		}

		return true;
	}

}
