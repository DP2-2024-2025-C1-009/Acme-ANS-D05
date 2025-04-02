
package acme.features.administrator.airline;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.airline.Airline;
import acme.entities.airline.AirlineType;

@GuiService
public class AirlineCreateService extends AbstractGuiService<Administrator, Airline> {

	@Autowired
	private AirlineRepository airlineRepository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Airline instance = new Airline();
		Date now = MomentHelper.getCurrentMoment();

		instance.setFoundationMoment(now);
		super.getBuffer().addData(instance);
	}

	@Override
	public void bind(final Airline airline) {
		super.bindObject(airline, "name", "iataCode", "website", "type", "foundationMoment", "email", "phoneNumber");
	}

	@Override
	public void validate(final Airline airline) {
		String code = airline.getIataCode();
		Collection<Airline> existing = this.airlineRepository.findAllAirlineCode(code);

		super.state(existing.isEmpty(), "iataCode", "customers.booking.error.repeat-code");

		boolean confirmed = super.getRequest().getData("confirmation", boolean.class);
		super.state(confirmed, "confirmation", "acme.validation.confirmation.message");
	}

	@Override
	public void perform(final Airline airline) {
		this.airlineRepository.save(airline);
	}

	@Override
	public void unbind(final Airline airline) {
		Dataset data = super.unbindObject(airline, "name", "iataCode", "website", "type", "foundationMoment", "email", "phoneNumber");
		data.put("types", SelectChoices.from(AirlineType.class, airline.getType()));
		data.put("confirmation", false);
		data.put("readonly", false);

		super.getResponse().addData(data);
	}
}
