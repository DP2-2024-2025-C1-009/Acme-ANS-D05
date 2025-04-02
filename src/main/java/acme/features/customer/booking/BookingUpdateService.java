
package acme.features.customer.booking;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.Booking;
import acme.entities.Booking.FlightClass;
import acme.realms.Customer;

@GuiService
public class BookingUpdateService extends AbstractGuiService<Customer, Booking> {

	@Autowired
	private BookingRepository bookingRepository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		int id = super.getResponse().getData("id", int.class);
		Booking booking = this.bookingRepository.findBookingById(id);
		super.getBuffer().addData(booking);
	}

	@Override
	public void bind(final Booking booking) {
		super.bindObject(booking, "locatorCode", "purchaseTime", "flightClass", "prize", "lastNibble");
	}

	@Override
	public void validate(final Booking booking) {
		Collection<Booking> res = this.bookingRepository.findAllBookings();

		if (res.contains(booking))
			super.state(false, "booking", "booking.already-exists");

		boolean confirmation;
		confirmation = super.getRequest().getData("confirmation", boolean.class);
		super.state(confirmation, "confirmation", "acme.validation.confirmation.message");
	}

	@Override
	public void perform(final Booking booking) {
		this.bookingRepository.save(booking);
	}

	@Override
	public void unbind(final Booking booking) {
		Dataset data = super.unbindObject(booking, "locatorCode", "purchaseTime", "flightClass", "prize", "lastNibble");
		data.put("flightclass", SelectChoices.from(FlightClass.class, booking.getFlightClass()));
		data.put("confirmation", false);
		data.put("readonly", false);
		super.getResponse().addData(data);
	}

}
