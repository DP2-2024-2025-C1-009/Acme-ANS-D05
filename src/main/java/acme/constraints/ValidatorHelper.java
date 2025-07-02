
package acme.constraints;

import acme.client.components.principals.DefaultUserIdentity;
import acme.client.helpers.StringHelper;

public abstract class ValidatorHelper {

	protected ValidatorHelper() {

	}

	public static boolean checkFormatIsCorrect(final String identifier, final DefaultUserIdentity dui) {
		return identifier != null && dui != null && !StringHelper.isBlank(dui.getName()) && !StringHelper.isBlank(dui.getSurname()) && !StringHelper.isBlank(identifier) && identifier.trim().length() >= 2 && dui.getName().charAt(0) == identifier.charAt(0)
			&& dui.getSurname().charAt(0) == identifier.charAt(1);
	}

	public static boolean checkUniqueness(final Object inputIdentity, final Object obtainedIdentity) {
		return obtainedIdentity == null || obtainedIdentity.equals(inputIdentity);
	}

}
