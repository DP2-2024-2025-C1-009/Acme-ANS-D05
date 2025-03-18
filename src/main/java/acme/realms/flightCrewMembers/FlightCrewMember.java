
package acme.realms.flightCrewMembers;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;

import acme.client.components.basis.AbstractRole;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import acme.constraints.ValidIdentifier;
import acme.entities.airline.Airline;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class FlightCrewMember extends AbstractRole {

	// Serialisation version --------------------------------------------------

	private static final long		serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@ValidIdentifier //terminar
	@Column(unique = true)
	private String					employeeCode;

	@Mandatory
	@ValidString(pattern = "^\\+?\\d{6,15}$")
	@Automapped
	private String					phoneNumber;

	@Mandatory
	@ValidString(min = 20, max = 255)
	@Automapped
	private String					languageSkills;

	@Mandatory
	@Valid
	@Automapped
	private FlightCrewMemberStatus	flightCrewMemberStatus;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Airline					airline;

	@Mandatory
	@ValidMoney()
	@Automapped
	private Money					salary;

	@Optional
	@ValidNumber(min = 0, max = 120)
	@Automapped
	private Integer					yearsOfExperience;

}
