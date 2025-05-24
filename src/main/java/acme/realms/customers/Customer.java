
package acme.realms.customers;

import javax.persistence.Column;
import javax.persistence.Entity;

import acme.client.components.basis.AbstractRole;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import acme.constraints.ValidCustomer;
import acme.constraints.ValidIdentifier;
import acme.constraints.ValidPhoneNumber;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@ValidCustomer
public class Customer extends AbstractRole {

	// Serialisation identifier
	private static final long	serialVersionUID	= 1L;

	//Attributes

	@Mandatory
	@ValidIdentifier
	@Column(unique = true)
	private String				identifier;

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
