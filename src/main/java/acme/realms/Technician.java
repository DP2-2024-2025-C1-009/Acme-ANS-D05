
package acme.realms;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import acme.client.components.basis.AbstractRole;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Technician extends AbstractRole {

	// Serialisation identifier -----------------------------------------------
	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Column(unique = true)
	@Pattern(regexp = "^[A-Z]{2,3}\\d{6}$")
	@NotBlank
	private String				licenseNumber;

	@NotBlank
	@Pattern(regexp = "^\\+?\\d{6,15}$")
	private String				phoneNumber;

	@NotBlank
	@Length(max = 50)
	@Column(length = 50)
	private String				specialisation;

	private boolean				healthTestPassed;

	@Min(0)
	private int					yearsExperience;

	@Length(max = 255)
	private String				certifications;

}
