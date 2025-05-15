
package acme.features.customer.booking;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.Booking;
import acme.entities.booking.FlightClass;
import acme.realms.Customer;

@GuiService
public class BookingCreateService extends AbstractGuiService<Customer, Booking> {

	@Autowired
	private BookingRepository bookingRepository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Booking booking = new Booking();
		Date now = MomentHelper.getCurrentMoment();
		booking.setPurchaseTime(now);
		super.getBuffer().addData(booking);
	}

	@Override
	public void bind(final Booking booking) {
		super.bindObject(booking, "locatorCode", "purchaseTime", "flightClass", "prize", "lastNibble");
	}

	@Override
	public void validate(final Booking booking) {
		Collection<Booking> res = this.bookingRepository.findAllBookings();
		Integer lcn = booking.getLastNibble();

		if (!res.contains(lcn.intValue()))
			super.state(false, "lastCardNibble", "lastCardNibble.not-stored");

		boolean confirmation;
		confirmation = super.getResponse().getData("confirmation", boolean.class);
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
