
package acme.realms;

import javax.persistence.Entity;
import javax.validation.constraints.Pattern;

import acme.client.components.basis.AbstractRole;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import acme.constraints.ValidPhoneNumber;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Customer extends AbstractRole {

	// Serialisation identifier
	private static final long	serialVersionUID	= 1L;

	//Attributes

	@Mandatory
	@ValidString(min = 8, max = 9)
	@Pattern(regexp = "^[A-Z]{2,3}\\d{6}$", message = "Customer Identificator " + "must have 2/3 letters followed by 6 numbers")
	@Automapped
	private String				customerId;

	@Mandatory
	@ValidPhoneNumber
	@Automapped
	private String				telephoneNumber;

	@Mandatory
	@ValidString(max = 255)
	@Automapped
	private String				address;

	@Mandatory
	@ValidString(max = 50)
	@Automapped
	private String				city;

	@Mandatory
	@ValidString(max = 50)
	@Automapped
	private String				country;

	@Optional
	@ValidNumber(max = 500000)
	@Automapped
	private Integer				accumulatedPoints;

}
