
package acme.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Pattern;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidString;
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
	@ValidString(max = 255)
	@Automapped
	private String				fullName;

	@Mandatory
	@ValidString
	@Automapped
	private String				email;

	@Mandatory
	@Pattern(regexp = "^[A-Z0-9]{6,9}$", message = "The passport pattern must be followed")
	@ValidString
	@Automapped
	private String				pasport;

	@Mandatory
	@Temporal(TemporalType.DATE)
	@ValidMoment(past = true)
	@Automapped
	private Date				birthDate;

	@Optional
	@ValidString(max = 50)
	@Automapped
	private String				specialNeeds;

}
