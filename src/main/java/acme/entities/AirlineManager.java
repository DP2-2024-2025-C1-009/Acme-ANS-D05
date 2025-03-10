
package acme.entities;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.Min;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;

import acme.client.components.basis.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class AirlineManager extends AbstractEntity {

	@Column(unique = true, nullable = false)
	@Pattern(regexp = "^[A-Z]{2,3}\\d{6}$", message = "Identifier number must have 2 or 3 uppercase letters followed by 6 digits")
	private String		identifierNumber;

	@Column(nullable = false)
	@Min(value = 0, message = "Years of experience must be at least 0")
	private Integer		yearsOfExperience;

	@Column(nullable = false)
	@Past(message = "Date of birth date must be in the past")
	private LocalDate	dateOfBirth;

	@Column(nullable = true, length = 512)
	private String		pictureLink;
}
