
package acme.realms;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.validation.constraints.Min;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;

import acme.client.components.basis.AbstractRole;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import acme.client.components.validation.ValidUrl;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Manager extends AbstractRole {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@ValidString
	@Pattern(regexp = "^[A-Z]{2,3}\\d{6}$", message = "Identifier number must have 2 or 3 uppercase letters followed by 6 digits")
	private String				identifierNumber;

	@Mandatory
	@ValidNumber
	@Min(value = 0, message = "Years of experience must be at least 0")
	private Integer				yearsOfExperience;

	@Mandatory
	@ValidMoment
	@Past(message = "Date of birth date must be in the past")
	private LocalDate			dateOfBirth;

	@ValidUrl
	private String				pictureLink;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------
}
