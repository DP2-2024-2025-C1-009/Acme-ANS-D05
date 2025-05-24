
package acme.features.customer.booking;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.Booking;
import acme.entities.booking.FlightClass;
import acme.entities.flight.Flight;
import acme.realms.customers.Customer;

@GuiService
public class BookingUpdateService extends AbstractGuiService<Customer, Booking> {

	@Autowired
	private CustomerBookingRepository bookingRepository;


	@Override
	public void authorise() {
		boolean status;
		int masterId;
		Booking booking;
		Customer customer;

		masterId = super.getRequest().getData("id", int.class);
		booking = this.bookingRepository.findBookingById(masterId);
		customer = booking == null ? null : booking.getCustomer();
		status = booking != null && booking.isDraftMode() && super.getRequest().getPrincipal().hasRealm(customer);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Booking booking;
		int id;

		id = super.getRequest().getData("id", int.class);
		booking = this.bookingRepository.findBookingById(id);

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
		Dataset dataset;
		SelectChoices classChoise;
		SelectChoices flightChoice;
		Collection<Flight> flights;

		flights = this.bookingRepository.findAllFlightsDraftModeFalse();
		flightChoice = SelectChoices.from(flights, "id", booking.getFlight());
		classChoise = SelectChoices.from(FlightClass.class, booking.getFlightClass());

		dataset = super.unbindObject(booking, "locatorCode", "purchaseMoment", "lastCardNibble", "draftMode");
		dataset.put("classChoise", classChoise);
		dataset.put("bookingCost", booking.getCost());
		dataset.put("flightChoice", flightChoice);
		super.getResponse().addData(dataset);
	}

}
