
package acme.features.administrator.aircraft;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.aircraft.Aircraft;
import acme.entities.airline.Airline;
import acme.features.administrator.airline.AirlineRepository;

@GuiService
public class AircraftUpdateService extends AbstractGuiService<Administrator, Aircraft> {

	@Autowired
	private AircraftRepository	aircraftRepository;

	@Autowired
	private AirlineRepository	airlineRepository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		int id = super.getRequest().getData("id", int.class);
		Aircraft aircraft = this.aircraftRepository.findAircraftById(id);
		super.getBuffer().addData(aircraft);
	}

	@Override
	public void bind(final Aircraft aircraft) {
		super.bindObject(aircraft, "model", "numberRegistration", "numberPassengers", "loadWeight", "isActive", "optionalDetails");
		Integer airlineId = super.getRequest().getData("airline", Integer.class);
		if (airlineId != null) {
			Airline airline = this.airlineRepository.findAirlineById(airlineId);
			aircraft.setAirline(airline);
		}
	}

	@Override
	public void validate(final Aircraft aircraft) {
		boolean confirmation = super.getRequest().getData("confirmation", boolean.class);
		super.state(confirmation, "confirmation", "acme.validation.confirmation.message");
	}

	@Override
	public void perform(final Aircraft aircraft) {
		this.aircraftRepository.save(aircraft);
	}

	@Override
	public void unbind(final Aircraft aircraft) {
		Dataset data = super.unbindObject(aircraft, "model", "numberRegistration", "numberPassengers", "loadWeight", "isActive", "optionalDetails");

		Collection<Airline> airlines = this.airlineRepository.findAllAirlines();
		SelectChoices choices = SelectChoices.from(airlines, "name", aircraft.getAirline());

		data.put("airlines", choices);
		data.put("airline", aircraft.getAirline() != null ? String.valueOf(aircraft.getAirline().getId()) : "");

		super.getResponse().addData(data);
	}
}
