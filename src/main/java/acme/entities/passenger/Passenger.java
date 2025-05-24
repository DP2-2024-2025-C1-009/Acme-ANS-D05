
package acme.entities.passenger;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidEmail;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidString;
import acme.realms.customers.Customer;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter

public class Passenger extends AbstractEntity {

	// Serialisation identifier
	private static final long	serialVersionUID	= 1L;

	//Attributes

	@Mandatory
	@ValidString(min = 1, max = 255)
	@Automapped
	private String				fullName;

	@Mandatory
	@ValidEmail
	@Automapped
	private String				email;

	@Mandatory
	@Pattern(regexp = "^[A-Z0-9]{6,9}$", message = "{acme.validation.passenger.passport}")
	@ValidString
	@Column(unique = true)
	private String				passport;

	@Mandatory
	@Temporal(TemporalType.DATE)
	@ValidMoment(past = true)
	private Date				birthDate;

	@Optional
	@ValidString(max = 50)
	@Automapped
	private String				specialNeeds;

	@Mandatory
	@Automapped
	private boolean				draftMode;

	//Relations

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Customer			customer;

}
