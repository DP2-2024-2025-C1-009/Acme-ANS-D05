
package acme.entities;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

import acme.client.components.basis.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ActivityLog extends AbstractEntity {

	@Column(nullable = false)
	@Past(message = "Registration moment must be in the past")
	private LocalDateTime		registrationMoment;

	@Column(nullable = false, length = 50)
	@NotBlank(message = "Incident type cannot be blank")
	@Size(max = 50, message = "Incident type must be at most 50 characters long")
	private String				incidentType;

	@Column(nullable = false, length = 255)
	@NotBlank(message = "Description cannot be blank")
	@Size(max = 255, message = "Description must be at most 255 characters long")
	private String				description;

	@Column(nullable = false)
	@Min(value = 0, message = "Severity level must be at least 0")
	@Max(value = 10, message = "Severity level must be at most 10")
	private int					severityLevel;

	@ManyToOne(optional = false)
	private FlightCrewMember	flightCrewMember;

	//@ManyToOne(optional = false)
	//private FlightLeg			flightLeg;
}
