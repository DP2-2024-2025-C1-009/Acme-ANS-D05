
package acme.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidEmail;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidString;
import acme.client.components.validation.ValidUrl;
import acme.constraints.ValidIATACode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Airline extends AbstractEntity {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@ValidString(min = 1, max = 50, message = "{acme.validation.airline.name-lenght}")
	@Automapped
	private String				name;

	@Column(unique = true)
	@Mandatory
	@ValidString(min = 1, max = 3, pattern = "^[A-Z]{3}$", message = "{acme.validation.airline.iata-code-pattern}")
	@ValidIATACode
	private String				iataCode;

	@Mandatory
	@ValidUrl(message = "{acme.validation.airline.website-valid}")
	@Automapped
	private String				website;

	@Mandatory
	@Valid
	@Automapped
	private AirlineType			type;

	@Mandatory
	@Temporal(TemporalType.TIMESTAMP)
	@ValidMoment(past = true, message = "{acme.validation.airline.foundation-moment-past}")
	private Date				foundationMoment;

	@Optional
	@ValidEmail(message = "{acme.validation.airline.email-valid}")
	@Automapped
	private String				email;

	@Optional
	@ValidString(pattern = "^\\+?\\d{6,15}$", message = "{acme.validation.airline.phone-number-pattern}")
	@Automapped
	private String				phoneNumber;

	// Airline Type Enum  -------------------------------------------------------------


	public enum AirlineType {
		LUXURY, STANDARD, LOW_COST
	}
}
