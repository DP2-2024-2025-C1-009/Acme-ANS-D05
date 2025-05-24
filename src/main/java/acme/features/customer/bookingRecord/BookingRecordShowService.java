
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
public class BookingRecordShowService extends AbstractGuiService<Customer, BookingRecord> {

	@Autowired
	private BookingRecordRepository bookingRecordRepository;


	@Override
	public void authorise() {
		boolean status;
		int masterId;
		BookingRecord br;
		Customer customer;

		masterId = super.getRequest().getData("id", int.class);
		br = this.bookingRecordRepository.findBookingRecordById(masterId);
		customer = br == null ? null : br.getBooking().getCustomer();
		status = super.getRequest().getPrincipal().hasRealm(customer) && br != null;

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

		dataset = super.unbindObject(br, "booking.locatorCode", "passenger.fullName", "draftMode");
		dataset.put("bookingChoice", bookingChoice);
		dataset.put("passengerChoice", passengerChoice);
		super.getResponse().addData(dataset);
	}
}
