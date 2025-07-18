
package acme.realms;

import javax.persistence.Column;
import javax.persistence.Entity;

import acme.client.components.basis.AbstractRole;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import acme.constraints.ValidPhoneNumber;
import acme.constraints.ValidShortText;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Technician extends AbstractRole {

	// Serialisation identifier -----------------------------------------------
	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@ValidString(min = 8, max = 9, pattern = "^[A-Z]{2,3}\\d{6}$")
	@Column(unique = true)
	private String				licenseNumber;

	@Mandatory
	@ValidPhoneNumber
	@Column(unique = true)
	private String				phoneNumber;

	@Mandatory
	@ValidShortText
	@Automapped
	private String				specialisation;

	@Mandatory
	// @Valid por defecto
	@Automapped
	private boolean				healthTestPassed;

	@Mandatory
	@ValidNumber(min = 0, max = 120)
	@Automapped
	private int					yearsExperience;

	@Optional
	@ValidString(min = 1, max = 255)
	@Automapped
	private String				certifications;

}
