
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.airport.Airport;
import acme.entities.airport.AirportRepository;

@Validator
public class IATACodeValidator extends AbstractValidator<ValidIATACode, Airport> {

	private static final String	IATA_REGEX	= "^[A-Z]{3}$";

	@Autowired
	private AirportRepository	airportRepository;


	@Override
	public boolean isValid(final Airport airport, final ConstraintValidatorContext context) {
		if (airport == null || airport.getIataCode() == null || airport.getIataCode().isEmpty()) {
			super.state(context, false, "iataCode", "IATA code cannot be empty");
			return false;
		}

		String iataCode = airport.getIataCode();

		if (!iataCode.matches(IATACodeValidator.IATA_REGEX)) {
			super.state(context, false, "iataCode", "IATA code must be three uppercase letters");
			return false;
		}

		int id = airport.getId();
		long count = this.airportRepository.countByIataCodeExcludingId(iataCode, id);

		if (count > 0) {
			super.state(context, false, "iataCode", "IATA code must be unique");
			return false;
		}

		return true;
	}

}
