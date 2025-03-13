
package acme.entities.airport;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidEmail;
import acme.client.components.validation.ValidUrl;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Airport extends AbstractEntity {

	// Serialisation identifier
	private static final long	serialVersionUID	= 1L;

	// Attributes 

	@Mandatory
	@Column(nullable = false, length = 50)
	private String				airportName;

	@Column(unique = true, nullable = false, length = 3)
	@Pattern(regexp = "^[A-Z]{3}$", message = "IATA code must be exactly three uppercase letters")
	private String				iataCode;

	@Mandatory
	@Valid
	@Automapped
	private OperationalScope	operationalScope;

	@Column(nullable = false, length = 50)
	@Automapped
	private String				city;

	@Column(nullable = false, length = 50)
	@Automapped
	private String				country;

	@Column(nullable = true, length = 100)
	@ValidUrl
	private String				website;

	@Column(nullable = true, length = 100)
	@ValidEmail
	@Automapped
	private String				email;

	@Optional
	//@Pattern(regexp = "^\\+?\\d{6,15}$", message = "Phone number must contain between 6 and 15 digits, optionally starting with '+'")
	@Automapped
	private String				contactPhoneNumber;

}
