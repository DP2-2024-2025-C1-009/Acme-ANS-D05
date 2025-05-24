
package acme.features.customer.passenger;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.passenger.Passenger;
import acme.realms.customers.Customer;

@GuiController
public class PassengerController extends AbstractGuiController<Customer, Passenger> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private PassengerListService		listService;

	@Autowired
	private PassengerBookingListService	listBookingService;

	@Autowired
	private PassengerShowService		showService;

	@Autowired
	private PassengerCreateService		createService;

	@Autowired
	private PassengerUpdateService		updateService;

	@Autowired
	private PassengerPublishService		publishService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addCustomCommand("list-in-booking", "list", this.listBookingService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("update", this.updateService);

		super.addCustomCommand("publish", "update", this.publishService);
	}
}
