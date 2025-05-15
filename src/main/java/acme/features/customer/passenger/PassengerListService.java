
package acme.features.customer.passenger;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.passenger.Passenger;
import acme.realms.Customer;

@GuiService
public class PassengerListService extends AbstractGuiService<Customer, Passenger> {

	@Autowired
	private PassengerRepository passengerRepository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Collection<Passenger> res = this.passengerRepository.findAllPassengers();
		super.getBuffer().addData(res);
	}

	@Override
	public void unbind(final Passenger passenger) {
		Dataset data = super.unbindObject(passenger, "fullName", "email", "passport", "birthDate", "specialNeeds");
		super.getResponse().addData(data);
	}
}
