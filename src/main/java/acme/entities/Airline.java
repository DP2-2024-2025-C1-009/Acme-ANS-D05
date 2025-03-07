
package acme.entities;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Email;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.URL;

import acme.client.components.basis.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Airline extends AbstractEntity {

	@Column(nullable = false, length = 50)
	private String		name;

	@Column(unique = true, nullable = false, length = 3)
	@Pattern(regexp = "^[A-Z]{3}$", message = "IATA code must be exactly three uppercase letters")
	private String		iataCode;

	@URL(message = "Website should be a valid URL")
	@Column(nullable = true)
	private String		website;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private AirlineType	type;

	@Column(nullable = false)
	@Past(message = "Foundation date must be in the past")
	private LocalDate	foundationMoment;

	@Email(message = "Email should be valid")
	@Column(nullable = true)
	private String		email;

	@Pattern(regexp = "^\\+?\\d{6,15}$", message = "Phone number must contain between 6 and 15 digits, optionally starting with '+'")
	@Column(nullable = true)
	private String		phoneNumber;


	public enum AirlineType {
		LUXURY, STANDARD, LOW_COST
	}
}
