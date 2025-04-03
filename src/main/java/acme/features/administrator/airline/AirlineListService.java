
package acme.features.administrator.airline;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.airline.Airline;

@GuiService
public class AirlineListService extends AbstractGuiService<Administrator, Airline> {

	@Autowired
	private AirlineRepository airlineRepository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Collection<Airline> results = this.airlineRepository.findAllAirlines();
		super.getBuffer().addData(results);
	}

	@Override
	public void unbind(final Airline airline) {
		Dataset data = super.unbindObject(airline, "name", "iataCode", "website", "foundationMoment", "type", "email", "phoneNumber");
		super.getResponse().addData(data);
	}
}
