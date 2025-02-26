
package acme.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

import acme.client.components.basis.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class FlightCrewMember extends AbstractEntity {

	@Column(unique = true, nullable = false)
	@Pattern(regexp = "^[A-Z]{2,3}\\d{6}$", message = "Employee code must have 2 or 3 uppercase letters followed by 6 digits")
	private String				employeeCode;

	@Column(nullable = false)
	@Pattern(regexp = "^\\+?\\d{6,15}$", message = "Phone number must contain between 6 and 15 digits, optionally starting with '+'")
	private String				phoneNumber;

	@Column(nullable = false, length = 255)
	private String				languageSkills;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private AvailabilityStatus	availabilityStatus;

	@ManyToOne(optional = false)
	private Airline				airline;

	@Column(nullable = false)
	@Min(value = 0, message = "Salary must be at least 0")
	private double				salary;

	@Column(nullable = true)
	@Min(value = 0, message = "Years of experience must be at least 0")
	private Integer				yearsOfExperience;


	public enum AvailabilityStatus {
		AVAILABLE, ON_VACATION, ON_LEAVE
	}
}
