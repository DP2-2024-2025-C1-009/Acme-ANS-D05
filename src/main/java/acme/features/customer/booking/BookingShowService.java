
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
public class BookingShowService extends AbstractGuiService<Customer, Booking> {

	@Autowired
	private BookingRepository bookingRepository;


	@Override
	public void authorise() {
		boolean status;
		int masterId;
		Booking booking;
		Customer customer;

		masterId = super.getRequest().getData("id", int.class);
		booking = this.bookingRepository.findBookingById(masterId);
		customer = booking == null ? null : booking.getCustomer();
		status = super.getRequest().getPrincipal().hasRealm(customer) && booking != null;

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
	public void unbind(final Booking booking) {
		Dataset dataset;
		SelectChoices flightChoice;
		SelectChoices classChoise;
		Collection<Flight> flights;

		flights = this.bookingRepository.findAllFlightsDraftModeFalse();
		flightChoice = SelectChoices.from(flights, "id", booking.getFligth());
		classChoise = SelectChoices.from(FlightClass.class, booking.getFlightClass());

		dataset = super.unbindObject(booking, "locatorCode", "purchaseMoment", "travelClass", "lastCardNibble", "flight", "draftMode");
		dataset.put("bookingCost", booking.getBookingCost());
		dataset.put("classChoise", classChoise);
		dataset.put("flightChoice", flightChoice);
		super.getResponse().addData(dataset);
	}
}
