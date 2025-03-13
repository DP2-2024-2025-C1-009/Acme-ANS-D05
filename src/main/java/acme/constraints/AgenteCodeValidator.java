
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.realms.AssistanceAgent;

@Validator
public class AgenteCodeValidator extends AbstractValidator<ValidAgentCode, AssistanceAgent> {

	@Override
	protected void initialise(final ValidAgentCode annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final AssistanceAgent agent, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;
		boolean isNull;

		isNull = agent == null || agent.getEmployeeCode() == null;

		if (!isNull) {
			String employeeCode = agent.getEmployeeCode();
			String letterPart = employeeCode.replaceAll("\\d", "");

			String name = agent.getUserAccount().getIdentity().getName();
			String surname = agent.getUserAccount().getIdentity().getSurname();
			if (name != null && !name.isEmpty() && surname != null && !surname.isEmpty()) {
				String initials = surname.substring(0, 1).toUpperCase() + name.substring(0, 1).toUpperCase();
				boolean valid = letterPart.startsWith(initials);
				super.state(context, valid, "employeeCode", "acme.validation.agent.employee-code-initials");
			}
		}

		result = !super.hasErrors(context);
		return result;
	}

}
