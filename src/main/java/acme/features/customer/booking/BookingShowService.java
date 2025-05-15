
package acme.features.customer.booking;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.Booking;
import acme.realms.Customer;

@GuiService
public class BookingShowService extends AbstractGuiService<Customer, Booking> {

	@Autowired
	private BookingRepository bookingRepository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		int id = super.getRequest().getData("id", int.class);
		Booking res = this.bookingRepository.findBookingById(id);
		super.getBuffer().addData(res);
	}

	@Override
	public void unbind(final Booking booking) {
		Dataset data = super.unbindObject(booking, "locatorCode", "purchaseTime");

		data.put("flightClass", booking.getFlightClass().toString());

		data.put("prize", booking.getPrize() != null ? booking.getPrize() : "");
		data.put("lastNibble", booking.getLastNibble() != null ? booking.getLastNibble() : "");

		super.getResponse().addData(data);
	}
}
