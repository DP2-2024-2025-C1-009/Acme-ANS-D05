
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.booking.Booking;
import acme.entities.booking.BookingRepository;

@Validator
public class BookingValidator extends AbstractValidator<ValidBooking, Booking> {

	@Autowired
	private BookingRepository bookingRepository;


	@Override
	protected void initialise(final ValidBooking annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Booking booking, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result = true;

		if (booking.isDraftMode() == false) {

			int passengerCount = this.bookingRepository.countPassengersByLocatorCode(booking.getLocatorCode());
			boolean hasPassengers = passengerCount > 0;
			super.state(context, hasPassengers, "*", "acme.validation.booking.passenger-required.message");

			boolean hasCardNibble = booking.getLastCardNibble() != null && !booking.getLastCardNibble().isEmpty();
			super.state(context, hasCardNibble, "*", "acme.validation.booking.card-nibble-required.message");
		}

		result = !super.hasErrors(context);
		return result;
	}
}
