
package acme.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.validation.Optional;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Booking extends AbstractEntity {

	@Size(min = 6, max = 8)
	@Column(nullable = false)
	@Pattern(regexp = "^[A-Z0-9]{6,8}$")
	private String		locatorCode;

	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@Past
	private Date		purchaseTime;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private FlightClass	flightClass;

	@Column(nullable = false)
	private Double		prize;

	@Optional
	@Column(nullable = true, length = 4)
	private Integer		lastNibble;


	public enum FlightClass {
		ECONOMY, BUSINESS
	}

}
