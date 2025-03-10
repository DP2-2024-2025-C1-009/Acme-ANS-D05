
package acme.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.validation.Optional;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter

public class Aircraft extends AbstractEntity {

	@Size(max = 50)
	@Column(nullable = false)
	private String			model;

	@Size(max = 50)
	@Column(nullable = false, unique = true)
	private String			numberRegistration;

	@Column(nullable = false)
	private Integer			numberPassengers;

	@Min(2000)
	@Max(50000)
	@Column(nullable = false)
	private Integer			loadWeight;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private AircraftState	state;

	@Optional
	@Size(max = 255)
	@Column(nullable = true)
	private String			optionalDetails;


	public enum AircraftState {
		SERVICE, ACTIVE, MAINTENANCE
	}

}
