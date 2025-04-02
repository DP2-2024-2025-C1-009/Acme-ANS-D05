
package acme.features.administrator.aircraft;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.principals.Administrator;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.aircraft.Aircraft;

@GuiService
public class AircraftDisableService extends AbstractGuiService<Administrator, Aircraft> {

	@Autowired
	private AircraftRepository aircraftRepository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Aircraft aircraft;
		int id;

		id = super.getRequest().getData("id", int.class);
		aircraft = this.aircraftRepository.findAircraftById(id);
		super.getBuffer().addData(aircraft);
	}

	//Falta terminar de hacer que se deshabilite el avion y hacer el unbind
	@Override
	public void validate(final Aircraft aircraft) {
		boolean canDisable = true;
		int id = aircraft.getId();
		boolean state = aircraft.getIsActive();

		//If its not active, can´t disable
		if (!state)
			canDisable = false;

	}

}
