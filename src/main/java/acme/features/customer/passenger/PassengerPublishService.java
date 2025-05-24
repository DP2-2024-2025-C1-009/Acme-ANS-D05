
package acme.features.customer.passenger;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.passenger.Passenger;
import acme.realms.customers.Customer;

@GuiService
public class PassengerPublishService extends AbstractGuiService<Customer, Passenger> {

	@Autowired
	private PassengerRepository passengerRepository;


	@Override
	public void authorise() {
		boolean status;
		int passengerId;
		Passenger passenger;
		Customer customer;

		passengerId = super.getRequest().getData("id", int.class);
		passenger = this.passengerRepository.findPassengerById(passengerId);

		customer = passenger == null ? null : this.passengerRepository.findCustomerByPassengerId(passenger.getId());

		status = passenger != null && passenger.isDraftMode() && super.getRequest().getPrincipal().hasRealm(customer);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Passenger passenger;
		int id;

		id = super.getRequest().getData("id", int.class);
		passenger = this.passengerRepository.findPassengerById(id);

		super.getBuffer().addData(passenger);
	}

	@Override
	public void bind(final Passenger passenger) {
		super.bindObject(passenger, "fullName", "email", "passport", "birthDate", "specialNeeds");
	}

	@Override
	public void validate(final Passenger passenger) {
		;
	}

	@Override
	public void perform(final Passenger passenger) {
		passenger.setDraftMode(false);
		this.passengerRepository.save(passenger);
	}

	@Override
	public void unbind(final Passenger passenger) {
		Dataset dataset;

		dataset = super.unbindObject(passenger, "fullName", "email", "passport", "birthDate", "specialNeeds", "draftMode");
		super.getResponse().addData(dataset);
	}
}
