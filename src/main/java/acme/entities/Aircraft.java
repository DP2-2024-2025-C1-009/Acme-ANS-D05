
package acme.entities;

import javax.persistence.Column;
import javax.persistence.Entity;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter

public class Aircraft extends AbstractEntity {

	// Serialisation identifier
	private static final long	serialVersionUID	= 1L;

	// Attributes 

	@Mandatory
	@ValidString(min = 1, max = 50)
	@Automapped
	private String				model;

	@Mandatory
	@ValidString(min = 1, max = 50)
	@Column(unique = true)
	private String				numberRegistration;

	@Mandatory
	@ValidNumber(min = 1, max = 255)
	@Automapped
	private Integer				numberPassengers;

	@Mandatory
	private Integer				loadWeight;

	// REVISAR ESTO PARA QUE NO DE PROBLEMAS
	//	@Mandatory
	//	@Valid
	//	@Automapped
	//	private AircraftState		state;

	@Optional
	@ValidString(min = 0, max = 255)
	@Automapped
	private String				optionalDetails;


	public enum AircraftState {
		ACTIVE, MAINTENANCE
	}

}
