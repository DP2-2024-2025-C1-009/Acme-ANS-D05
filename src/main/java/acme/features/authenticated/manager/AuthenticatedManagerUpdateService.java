
package acme.features.authenticated.manager;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Authenticated;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.PrincipalHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.realms.Manager;

@GuiService
public class AuthenticatedManagerUpdateService extends AbstractGuiService<Authenticated, Manager> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AuthenticatedManagerRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;

		status = super.getRequest().getPrincipal().hasRealmOfType(Manager.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Manager manager;
		int userAccountId;

		userAccountId = super.getRequest().getPrincipal().getAccountId();
		manager = this.repository.findManagerByUserAccountId(userAccountId);

		super.getBuffer().addData(manager);
	}

	@Override
	public void bind(final Manager manager) {
		assert manager != null;

		super.bindObject(manager, "identifierNumber", "yearsOfExperience", "dateOfBirth", "pictureLink");
	}

	@Override
	public void validate(final Manager manager) {
		assert manager != null;
	}

	@Override
	public void perform(final Manager manager) {
		assert manager != null;

		this.repository.save(manager);
	}

	@Override
	public void unbind(final Manager manager) {
		Dataset dataset;

		SelectChoices airlineChoices = SelectChoices.from(this.repository.findAllAirlines(), "name", manager.getAirline());

		dataset = super.unbindObject(manager, "identifierNumber", "yearsOfExperience", "dateOfBirth", "pictureLink");

		dataset.put("airline", airlineChoices.getSelected().getKey());
		dataset.put("airlines", airlineChoices);

		super.getResponse().addData(dataset);
	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals("POST"))
			PrincipalHelper.handleUpdate();
	}
}
