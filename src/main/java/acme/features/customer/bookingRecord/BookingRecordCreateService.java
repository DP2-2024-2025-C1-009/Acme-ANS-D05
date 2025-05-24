
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
public class BookingRecordCreateService extends AbstractGuiService<Customer, BookingRecord> {

	@Autowired
	private BookingRecordRepository bookingRecordRepository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		BookingRecord br;

		br = new BookingRecord();
		br.setDraftMode(true);

		super.getBuffer().addData(br);
	}

	@Override
	public void bind(final BookingRecord br) {
		super.bindObject(br, "booking", "passenger");
	}

	@Override
	public void validate(final BookingRecord br) {
		if (br.getBooking() != null && br.getPassenger() != null) {
			BookingRecord existing = this.bookingRecordRepository.findByBookingIdAndPassengerId(br.getBooking().getId(), br.getPassenger().getId());
			super.state(existing == null, "passenger", "customer.booking-record.form.error.duplicate");
		}
	}

	@Override
	public void perform(final BookingRecord br) {
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
