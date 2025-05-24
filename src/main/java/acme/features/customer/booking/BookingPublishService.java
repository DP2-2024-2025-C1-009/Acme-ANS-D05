
package acme.features.customer.booking;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.Booking;
import acme.entities.booking.BookingRecord;
import acme.entities.booking.FlightClass;
import acme.entities.flight.Flight;
import acme.entities.passenger.Passenger;
import acme.realms.customers.Customer;

@GuiService
public class BookingPublishService extends AbstractGuiService<Customer, Booking> {

	@Autowired
	private CustomerBookingRepository bookingRepository;


	@Override
	public void authorise() {
		boolean status;
		int bookingId;
		Booking booking;
		Customer customer;

		bookingId = super.getRequest().getData("id", int.class);
		booking = this.bookingRepository.findBookingById(bookingId);
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
		super.bindObject(booking, "locatorCode", "purchaseMoment", "travelClass", "lastCardNibble", "flight");
	}

	@Override
	public void validate(final Booking booking) {
		int id = super.getRequest().getData("id", int.class);

		Collection<Passenger> passengers = this.bookingRepository.findPassengersByBookingId(id);
		if (passengers.isEmpty())
			super.state(false, "*", "customer.booking.publish.non-published-passengers");
		else
			for (Passenger passenger : passengers)
				if (passenger.isDraftMode()) {
					super.state(false, "*", "customer.booking.publish.non-published-passengers");
					break;
				}

		Collection<BookingRecord> bookingRecords = this.bookingRepository.findBookingRecordsByBookingId(id);
		for (BookingRecord br : bookingRecords)
			if (br.isDraftMode()) {
				super.state(false, "*", "customer.booking.publish.non-published-bookingrecords");
				break;
			}

		if (booking.getLastCardNibble() == null || booking.getLastCardNibble().trim().isEmpty())
			super.state(false, "lastCardNibble", "customer.booking.form.error.last-card-nible-required");
	}

	@Override
	public void perform(final Booking booking) {
		booking.setDraftMode(false);
		booking.setPurchaseTime(MomentHelper.getCurrentMoment());
		this.bookingRepository.save(booking);
	}

	@Override
	public void unbind(final Booking booking) {
		Dataset dataset;
		SelectChoices flightChoice;
		SelectChoices classChoise;
		Collection<Flight> flights;

		flights = this.bookingRepository.findAllFlightsDraftModeFalse();
		flightChoice = SelectChoices.from(flights, "id", booking.getFlight());
		classChoise = SelectChoices.from(FlightClass.class, booking.getFlightClass());

		dataset = super.unbindObject(booking, "locatorCode", "purchaseMoment", "travelClass", "lastCardNibble", "flight", "draftMode");
		dataset.put("bookingCost", booking.getCost());
		dataset.put("classChoise", classChoise);
		dataset.put("flightChoice", flightChoice);
		super.getResponse().addData(dataset);
	}
}
