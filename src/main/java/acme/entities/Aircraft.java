
package acme.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
<<<<<<< HEAD
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.Valid;
=======
>>>>>>> e2f00f82ed61c657e3b4ea6560f0d77b1d7812d2

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

<<<<<<< HEAD
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
	@Automapped
	private String				numberRegistration;

	@Mandatory
	@ValidNumber(min = 1, max = 255)
=======
	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@ValidString(min = 1, max = 50, message = "{acme.validation.aircraft.model-length}")
	@Automapped
	private String				model;

	@Mandatory
	@Column(unique = true)
	@ValidString(min = 1, max = 50, message = "{acme-validation.aircraft.number-registration-length}")
	private String				numberRegistration;

	@Mandatory
	@ValidNumber(min = 1, max = 255, message = "{acme.validation.aircraft.number-passengers-range}")
>>>>>>> e2f00f82ed61c657e3b4ea6560f0d77b1d7812d2
	@Automapped
	private Integer				numberPassengers;

	@Mandatory
<<<<<<< HEAD
	@ValidNumber(min = 2, max = 50)
=======
	@ValidNumber(min = 2000, max = 50000, message = "{acme.validation.aircraft.load-weigth-range}")
>>>>>>> e2f00f82ed61c657e3b4ea6560f0d77b1d7812d2
	@Automapped
	private Integer				loadWeight;

	@Mandatory
<<<<<<< HEAD
	@Enumerated(EnumType.STRING)
	@Valid
	@Automapped
	private AircraftState		state;

	@Optional
	@ValidString(min = 0, max = 255)
	@Automapped
	private String				optionalDetails;


	public enum AircraftState {
		ACTIVE, MAINTENANCE
	}
=======
	@Automapped
	private boolean				aIsActive;

	@Optional
	@ValidString(max = 255, message = "{acme.validation.aircraft.optional-details-length}")
	@Automapped
	private String				optionalDetails;
>>>>>>> e2f00f82ed61c657e3b4ea6560f0d77b1d7812d2

}
