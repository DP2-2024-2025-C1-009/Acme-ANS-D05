
package acme.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidString;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Leg extends AbstractEntity {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@ValidString
	@Pattern(regexp = "^[A-Z]{3}\\d{6}$", message = "Flight number must consist of the airline's IATA code followed by four digits")
	private String				flightNumber;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	private Date				scheduledDeparture;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	private Date				scheduledArrival;

	@Column(nullable = false)
	@Min(value = 0, message = "Duration must be at least 0 hours")
	private int					duration;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Status				status;

	@Mandatory
	@Valid
	//@ManyToOne(optional = false)
	Airport						departureAirport;

	@Mandatory
	@Valid
	//@ManyToOne(optional = false)
	Airport						arrivalAirport;

	@Mandatory
	@Valid
	//@ManyToOne(optional = false)
	Aircraft					aircraft;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	Flight						flight;


	public enum Status {
		ON_TIME, DELAYED, CANCELLED, LANDED
	}

	// hay que hacer un getDuration

}
