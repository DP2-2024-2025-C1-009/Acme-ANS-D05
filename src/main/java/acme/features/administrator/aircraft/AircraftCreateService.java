
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

// Corregir el crear (no da problemas pero no se encuentra al crearlo)
@GuiService
public class AircraftCreateService extends AbstractGuiService<Administrator, Aircraft> {

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
		Aircraft aircraft;
		aircraft = new Aircraft();
		super.getBuffer().addData(aircraft);
	}

	@Override
	public void bind(final Aircraft aircraft) {
		super.bindObject(aircraft, "model", "numberRegistration", "numberPassengers", "loadWeight", "isActive", "optionalDetails", "airline");
	}

	@Override
	public void validate(final Aircraft aircraft) {
		boolean confirmation;
		confirmation = super.getRequest().getData("confirmation", boolean.class);
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
		data.put("airline", choices.getSelected().getKey());

		data.put("confirmation", false);
		data.put("readonly", false);
		super.getResponse().addData(data);
	}

}
