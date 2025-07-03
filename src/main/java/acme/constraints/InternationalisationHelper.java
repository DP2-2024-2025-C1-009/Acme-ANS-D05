
package acme.constraints;

import acme.client.helpers.MessageHelper;

public class InternationalisationHelper {

	private InternationalisationHelper() {
		throw new UnsupportedOperationException("Utility class");
	}

	public static String internationalizeBoolean(final boolean bool) {
		return MessageHelper.getMessage(bool ? "acme.value.boolean.true" : "acme.value.boolean.false");
	}

}
