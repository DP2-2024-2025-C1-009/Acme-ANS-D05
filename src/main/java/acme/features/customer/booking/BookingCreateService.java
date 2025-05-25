
package acme.features.customer.booking;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.Booking;
import acme.entities.booking.FlightClass;
import acme.entities.flight.Flight;
import acme.realms.customers.Customer;

@GuiService
public class BookingCreateService extends AbstractGuiService<Customer, Booking> {

	@Autowired
	private CustomerBookingRepository bookingRepository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Booking booking;
		Customer customer;

		customer = (Customer) super.getRequest().getPrincipal().getActiveRealm();

		booking = new Booking();
		booking.setDraftMode(true);
		booking.setCustomer(customer);
		booking.setPurchaseTime(MomentHelper.getCurrentMoment());

		super.getBuffer().addData(booking);
	}

	@Override
	public void bind(final Booking booking) {
		super.bindObject(booking, "locatorCode", "purchaseTime", "flightClass", "prize", "lastCardNibble");
	}

	@Override
	public void validate(final Booking booking) {
		boolean confirmation = false;
		if (super.getRequest().hasData("confirmation", boolean.class))
			confirmation = super.getRequest().getData("confirmation", boolean.class);
		super.state(confirmation, "confirmation", "acme.validation.confirmation.message");
	}

	@Override
	public void perform(final Booking booking) {
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

		dataset = super.unbindObject(booking, "locatorCode", "purchaseTime", "flightClass", "lastCardNibble", "flight", "draftMode");
		dataset.put("bookingCost", booking.getCost());
		dataset.put("classChoise", classChoise);
		dataset.put("flightChoice", flightChoice);
		super.getResponse().addData(dataset);
	}

}
