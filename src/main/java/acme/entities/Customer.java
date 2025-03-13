
package acme.entities;

import javax.persistence.Entity;
import javax.validation.constraints.Pattern;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Customer extends AbstractEntity {

	// Serialisation identifier
	private static final long	serialVersionUID	= 1L;

	//Attributes

	@Mandatory
	@ValidString(min = 8, max = 9)
	@Pattern(regexp = "^[A-Z]{2-3}\\d{6}$", message = "Customer Identificator " + "must have 2/3 letters followed by 6 numbers")
	@Automapped
	private String				customerId;

	@Mandatory
	@ValidNumber(min = 6, max = 15)
	@Pattern(regexp = "^+?\\d{6,15}$", message = "Customer telephone numbre " + "can start with simbol + and must have between 6 and 15 digits")
	@Automapped
	private Integer				telephoneNumber;

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
