
package acme.entities;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import acme.client.components.validation.Optional;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter

public class Passenger {

	@Column(nullable = false)
	@Size(max = 255)
	private String		fullName;

	@Column(nullable = false)
	private String		email;

	@Pattern(regexp = "^[A-Z0-9]{6,9}$", message = "The passport pattern must be followed")
	@Column(nullable = false)
	private String		pasport;

	@Temporal(TemporalType.DATE)
	@Past
	@Column(nullable = false)
	private LocalDate	birthDate;

	@Optional
	@Size(max = 50)
	@Column(nullable = true)
	private String		specialNeeds;

}
