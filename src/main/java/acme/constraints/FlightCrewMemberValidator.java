
package acme.constraints;

import java.util.Optional;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.principals.DefaultUserIdentity;
import acme.client.components.principals.UserAccount;
import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.features.flightCrewMember.FlightCrewMemberRepository;
import acme.realms.flightCrewMembers.FlightCrewMember;

@Validator
public class FlightCrewMemberValidator extends AbstractValidator<ValidFlightCrewMember, FlightCrewMember> {

	@Autowired
	private FlightCrewMemberRepository repository;


	@Override
	protected void initialise(final ValidFlightCrewMember annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final FlightCrewMember member, final ConstraintValidatorContext context) {

		String identifier = member.getEmployeeCode();

		if (identifier == null)
			return false;
		if (identifier.length() < 2) {
			super.state(context, false, "*", "{acme.validation.flightCrewMember.nullornotpattern.message}");
			return false;
		}
		UserAccount userAccount = member.getUserAccount();
		if (userAccount == null || userAccount.getIdentity() == null)
			return false;
		DefaultUserIdentity identity = member.getUserAccount().getIdentity();

		if (identity.getName() == null || identity.getName().isBlank() || identity.getSurname() == null || identity.getSurname().isBlank())
			return false;

		String inicialNombre = String.valueOf(identity.getName().charAt(0)).toUpperCase();
		String inicialApellido = String.valueOf(identity.getSurname().charAt(0)).toUpperCase();

		String iniciales = inicialNombre + inicialApellido;

		String employeeCodeInitials = identifier.substring(0, 2);

		if (!iniciales.equals(employeeCodeInitials)) {
			super.state(context, false, "*", "{acme.validation.flightCrewMember.initials.mismatch}");
			return false;
		}

		Optional<FlightCrewMember> memberWithSameCode = this.repository.findOneMemberByEmployeeCode(identifier);
		if (memberWithSameCode.isPresent() && memberWithSameCode.get().getId() != member.getId()) {
			super.state(context, false, "*", "{acme.validation.flightCrewMember.employeeCode.not-unique}");
			return false;
		}

		return true;
	}

}
