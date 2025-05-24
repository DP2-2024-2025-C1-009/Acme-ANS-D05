
package acme.features.customer.booking;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.Booking;
import acme.entities.booking.FlightClass;
import acme.realms.Customer;

@GuiService
public class BookingUpdateService extends AbstractGuiService<Customer, Booking> {

	@Autowired
	private BookingRepository bookingRepository;


	@Override
	public void authorise() {
		int id = super.getRequest().getData("id", int.class);
		Booking booking = this.bookingRepository.findBookingById(id);

		boolean notStored = booking.getLastNibble() == null;
		super.getResponse().setAuthorised(notStored);
	}

	@Override
	public void load() {
		int id = super.getRequest().getData("id", int.class);
		Booking booking = this.bookingRepository.findBookingById(id);

		super.getBuffer().addData(booking);
	}

	@Override
	public void bind(final Booking booking) {
		super.bindObject(booking, "locatorCode", "flightClass", "prize", "lastNibble");
	}

	@Override
	public void validate(final Booking booking) {
		boolean confirmation = super.getRequest().getData("confirmation", boolean.class);
		super.state(confirmation, "confirmation", "acme.validation.confirmation.message");
	}

	@Override
	public void perform(final Booking booking) {
		this.bookingRepository.save(booking);
	}

	@Override
	public void unbind(final Booking booking) {
		Dataset data = super.unbindObject(booking, "locatorCode", "purchaseTime", "flightClass", "prize", "lastNibble");

		SelectChoices choices = new SelectChoices();
		for (FlightClass fc : FlightClass.values())
			choices.add(fc.name(), fc.name(), fc == booking.getFlightClass());

		data.put("flightclass", choices);

		data.put("flightClass", choices);

		data.put("confirmation", false);
		data.put("purchaseTimeReadonly", true);

		super.getResponse().addData(data);
	}

}
