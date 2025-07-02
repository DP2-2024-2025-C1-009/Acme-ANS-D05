
package acme.realms;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import acme.client.components.basis.AbstractRole;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidString;
import acme.client.components.validation.ValidUrl;
import acme.constraints.ValidAssistanceAgent;
import acme.entities.airline.Airline;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@ValidAssistanceAgent
@Table(indexes = {
	@Index(columnList = "employeeCode", unique = true)
})
public class AssistanceAgent extends AbstractRole {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@Column(unique = true)
	@ValidString(pattern = "^[A-Z]{2,3}\\d{6}$", message = "{acme.validation.agent.employee-code-pattern}")
	private String				employeeCode;

	@Mandatory
	@ValidString(min = 1, max = 255, message = "{acme.validation.agent.spoken-languages-length}")
	@Automapped
	private String				spokenLanguages;

	@Mandatory
	@Temporal(TemporalType.TIMESTAMP)
	@ValidMoment(past = true, message = "{acme.validation.agent.start-working-date}")
	private Date				startWorking;

	@Optional
	@ValidString(min = 0, max = 255, message = "{acme.validation.agent.bio-length}")
	@Automapped
	private String				bio;

	@Optional
	@ValidMoney
	@Automapped
	private Money				salary;

	@Optional
	@ValidUrl
	@Automapped
	private String				photoLink;

	// Relationships ----------------------------------------------------------

	@ManyToOne(optional = true)
	@Valid
	@Optional
	private Airline				airline;

}
