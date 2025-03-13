
package acme.entities.agents;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidEmail;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidString;
import acme.entities.legs.Leg;
import acme.realms.AssistanceAgent;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Claim extends AbstractEntity {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@Temporal(TemporalType.TIMESTAMP)
	@ValidMoment(past = true, message = "{acme.validation.claim.registration-moment-date}")
	private Date				registrationMoment;

	@Mandatory
	@ValidEmail(message = "{acme.validation.claim.passenger-email-correct}")
	@Automapped
	private String				passengerEmail;

	@Mandatory
	@ValidString(min = 1, max = 255, message = "{acme.validation.claim.description-length}")
	@Automapped
	private String				description;

	@Mandatory
	@Valid
	@Automapped
	private ClaimType			type;

	@Mandatory
	@Automapped
	private boolean				cIsAccepted;

	// Relationships ----------------------------------------------------------

	@ManyToOne(optional = false)
	@Valid
	@Mandatory
	private AssistanceAgent		registeredBy;

	@ManyToOne(optional = false)
	@Valid
	@Mandatory
	private Leg					leg;

	// Claim Type -------------------------------------------------------------


	public enum ClaimType {
		FLIGHT_ISSUES, LUGGAGE_ISSUES, SECURITY_INCIDENT, OTHER_ISSUES
	}

}
