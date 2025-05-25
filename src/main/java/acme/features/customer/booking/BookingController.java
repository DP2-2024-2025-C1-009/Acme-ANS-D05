
package acme.features.customer.booking;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.booking.Booking;
import acme.realms.customers.Customer;

@GuiController
public class BookingController extends AbstractGuiController<Customer, Booking> {

	@Autowired
	private BookingListService		listService;

	@Autowired
	private BookingShowService		showService;

	@Autowired
	private BookingCreateService	createService;

	@Autowired
	private BookingUpdateService	updateService;

	@Autowired
	private BookingPublishService	publishService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("update", this.updateService);
		super.addCustomCommand("publish", "update", this.publishService);
	}

}
