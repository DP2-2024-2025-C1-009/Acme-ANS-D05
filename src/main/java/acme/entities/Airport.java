
package acme.entities;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

import acme.client.components.basis.AbstractEntity;

public class Airport extends AbstractEntity {

	@Column(nullable = false, length = 50)
	private String				airportName;

	@Column(unique = true, nullable = false, length = 3)
	@Pattern(regexp = "^[A-Z]{3}$", message = "IATA code must be exactly three uppercase letters")
	private String				iataCode;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private OperationalScope	operationalScope;

	@Column(nullable = false, length = 50)
	private String				city;

	@Column(nullable = false, length = 50)
	private String				country;

	@Column(nullable = true, length = 100)
	private String				website;

	@Column(nullable = true, length = 100)
	@Email(message = "Email should be valid")
	private String				email;

	@Column(nullable = true)
	@Pattern(regexp = "^\\+?\\d{6,15}$", message = "Phone number must contain between 6 and 15 digits, optionally starting with '+'")
	private String				contactPhoneNumber;


	public enum OperationalScope {
		INTERNATIONAL, DOMESTIC, REGIONAL
	}

}
