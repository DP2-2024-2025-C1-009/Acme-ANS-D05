
package acme.entities.booking;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Booking extends AbstractEntity {

	// Serialisation identifier
	private static final long	serialVersionUID	= 1L;

	//Attributes

	@Mandatory
	@ValidString(min = 6, max = 8)
	@Pattern(regexp = "^[A-Z0-9]{6,8}$")
	@Column(unique = true)
	@Automapped
	private String				locatorCode;

	@Mandatory
	@Temporal(TemporalType.TIMESTAMP)
	@ValidMoment(past = true)
	@Automapped
	private Date				purchaseTime;

	@Mandatory
	@Valid
	@Automapped
	private FlightClass			flightClass;

	@Mandatory
	@ValidNumber
	@Automapped
	private Double				prize;

	@Optional
	@ValidNumber
	@Automapped
	private Integer				lastNibble;

}
