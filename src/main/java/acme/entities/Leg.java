
package acme.entities;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

import acme.client.components.basis.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Leg extends AbstractEntity {

	@Column(unique = true, nullable = false)
	@Pattern(regexp = "^[A-Z]{3}\\d{6}$", message = "Flight number must consist of the airline's IATA code followed by four digits")
	private String			flightNumber;

	@Column(nullable = false)
	private LocalDateTime	scheduledDeparture;

	@Column(nullable = false)
	private LocalDateTime	scheduledArrival;

	@Column(nullable = false)
	@Min(value = 0, message = "Duration must be at least 0 hours")
	private int				duration;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Status			status;

	@Column(nullable = false, length = 100)
	private String			departureAirport;

	@Column(nullable = false, length = 100)
	private String			arrivalAirport;

	@Column(nullable = false, length = 100)
	private String			aircraft;

	@ManyToOne(optional = false)
	private Flight			flight;


	public enum Status {
		ON_TIME, DELAYED, CANCELLED, LANDED
	}
}
