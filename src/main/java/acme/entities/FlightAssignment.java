
package acme.entities;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

import acme.client.components.basis.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class FlightAssignment extends AbstractEntity {

	@ManyToOne(optional = false)
	private FlightCrewMember	crewMember;

	//@ManyToOne(optional = false)
	//	private FlightLeg			flightLeg;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Duty				duty;

	@Past
	@Column(nullable = false)
	private LocalDateTime		lastUpdate;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private AssignmentStatus	status;

	@Size(max = 255)
	private String				remarks;


	public enum Duty {
		PILOT, CO_PILOT, LEAD_ATTENDANT, CABIN_ATTENDANT;
	}

	public enum AssignmentStatus {
		CONFIRMED, PENDING, CANCELLED;
	}
}
