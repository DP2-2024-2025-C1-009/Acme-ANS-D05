
package acme.features.customer.bookingRecord;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.Booking;
import acme.entities.booking.BookingRecord;
import acme.entities.passenger.Passenger;
import acme.realms.customers.Customer;

@GuiService
public class BookingRecordPublishService extends AbstractGuiService<Customer, BookingRecord> {

	@Autowired
	private BookingRecordRepository bookingRecordRepository;


	@Override
	public void authorise() {
		boolean status;
		int brId;
		BookingRecord br;
		Customer customer;

		brId = super.getRequest().getData("id", int.class);
		br = this.bookingRecordRepository.findBookingRecordById(brId);
		customer = br == null ? null : br.getBooking().getCustomer();
		status = br != null && br.isDraftMode() && super.getRequest().getPrincipal().hasRealm(customer);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		BookingRecord br;
		int id;

		id = super.getRequest().getData("id", int.class);
		br = this.bookingRecordRepository.findBookingRecordById(id);

		super.getBuffer().addData(br);
	}

	@Override
	public void bind(final BookingRecord br) {
		super.bindObject(br, "booking", "passenger");
	}

	@Override
	public void validate(final BookingRecord br) {
		if (br.getPassenger() != null && br.getPassenger().isDraftMode())
			super.state(false, "passenger", "customer.booking-record.error.passenger-draft");
	}

	@Override
	public void perform(final BookingRecord br) {
		br.setDraftMode(false);
		this.bookingRecordRepository.save(br);
	}

	@Override
	public void unbind(final BookingRecord br) {
		Dataset dataset;
		SelectChoices bookingChoice;
		Collection<Booking> bookings;
		SelectChoices passengerChoice;
		Collection<Passenger> passengers;

		Integer customer;
		customer = super.getRequest().getPrincipal().getActiveRealm().getId();

		bookings = this.bookingRecordRepository.findAllBookingsByCustomerId(customer);
		bookingChoice = SelectChoices.from(bookings, "locatorCode", br.getBooking());

		passengers = this.bookingRecordRepository.findAllPassengersByCustomerId(customer);
		passengerChoice = SelectChoices.from(passengers, "fullName", br.getPassenger());

		dataset = super.unbindObject(br, "booking", "passenger", "draftMode");
		dataset.put("bookingChoice", bookingChoice);
		dataset.put("passengerChoice", passengerChoice);

		super.getResponse().addData(dataset);
	}
}
