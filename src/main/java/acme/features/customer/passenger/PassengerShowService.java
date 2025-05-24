
package acme.features.customer.passenger;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.passenger.Passenger;
import acme.realms.customers.Customer;

@GuiService
public class PassengerShowService extends AbstractGuiService<Customer, Passenger> {

	@Autowired
	private PassengerRepository passengerRepository;


	@Override
	public void authorise() {
		boolean status;
		int masterId;
		Passenger passenger;
		Customer customer;

		masterId = super.getRequest().getData("id", int.class);
		passenger = this.passengerRepository.findPassengerById(masterId);

		customer = passenger == null ? null : this.passengerRepository.findCustomerByPassengerId(passenger.getId());

		status = passenger != null && super.getRequest().getPrincipal().hasRealm(customer);

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
	public void unbind(final Passenger passenger) {
		Dataset dataset;

		dataset = super.unbindObject(passenger, "fullName", "email", "passport", "birthDate", "specialNeeds", "draftMode");
		super.getResponse().addData(dataset);
	}

}
