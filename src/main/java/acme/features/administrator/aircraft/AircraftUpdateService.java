
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
		int id = super.getResponse().getData("id", int.class);
		Aircraft aircraft = this.aircraftRepository.findAircraftById(id);

		if (aircraft == null)
			throw new IllegalStateException("Aircraft not found with ID: " + id);

		super.getBuffer().addData(aircraft);
	}

	@Override
	public void bind(final Aircraft aircraft) {
		super.bindObject(aircraft, "model", "numberRegistration", "numberPassengers", "loadWeight", "isActive", "optionalDetails", "airline");
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
		SelectChoices choices = new SelectChoices();

		for (Airline airline : airlines) {
			String key = String.valueOf(airline.getId());
			String label = airline.getName();
			boolean selected = aircraft.getAirline() != null && aircraft.getAirline().getId() == airline.getId();
			choices.add(key, label, selected);
		}

		data.put("airlines", choices);
		data.put("airline", choices.getSelected() != null ? choices.getSelected().getKey() : "");

		data.put("confirmation", false);
		data.put("readonly", false);
		super.getResponse().addData(data);
	}
}
