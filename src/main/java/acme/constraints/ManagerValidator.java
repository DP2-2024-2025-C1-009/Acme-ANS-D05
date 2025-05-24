
package acme.constraints;

import java.util.Date;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.principals.DefaultUserIdentity;
import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.MomentHelper;
import acme.client.helpers.StringHelper;
import acme.realms.Manager;
import acme.realms.ManagerRepository;

@Validator
public class ManagerValidator extends AbstractValidator<ValidManager, Manager> {

	@Autowired
	private ManagerRepository repository;


	@Override
	protected void initialise(final ValidManager annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Manager manager, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;

		if (manager == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {
			{
				boolean uniqueManager;
				Manager existingManager;

				existingManager = this.repository.findManagerByIdentifier(manager.getIdentifierNumber());
				uniqueManager = existingManager == null || existingManager.equals(manager);

				super.state(context, uniqueManager, "identifierNumber", "acme.validation.manager.duplicated-identifier.message");
			}
			{

				DefaultUserIdentity identity = manager.getIdentity();
				String iniciales = identity.getName().trim().substring(0, 1) + identity.getSurname().trim().substring(0, 1);

				Boolean identificadorCorrecto = StringHelper.startsWith(manager.getIdentifierNumber(), iniciales, true);

				super.state(context, identificadorCorrecto, "identifierNumber", "acme.validation.manager.intitials.message");
			}
			{
				boolean pastDate;
				Date present = MomentHelper.getBaseMoment();

				pastDate = manager.getDateOfBirth().before(present);
				super.state(context, pastDate, "dateOfBirth", "acme.validation.manager.birth-date");
			}
		}

		result = !super.hasErrors(context);

		return result;

	}
}
