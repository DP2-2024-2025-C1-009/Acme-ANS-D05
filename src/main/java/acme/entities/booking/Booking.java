
package acme.entities.booking;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import org.springframework.data.annotation.Transient;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidNumber;
import acme.client.helpers.SpringHelper;
import acme.constraints.ValidBooking;
import acme.constraints.ValidLastCardNibble;
import acme.constraints.ValidLocatorCode;
import acme.entities.flight.Flight;
import acme.realms.customers.Customer;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@ValidBooking
public class Booking extends AbstractEntity {

	// Serialisation identifier
	private static final long	serialVersionUID	= 1L;

	//Attributes

	@Mandatory
	@ValidLocatorCode
	@Column(unique = true)
	private String				locatorCode;

	@Mandatory
	@Temporal(TemporalType.TIMESTAMP)
	@ValidMoment(past = true)
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
	@ValidLastCardNibble
	@Automapped
	private String				lastCardNibble;

	@Mandatory
	@Automapped
	private boolean				draftMode;

	//Relations

	@Mandatory
	@ManyToOne(optional = false)
	private Customer			customer;

	@Mandatory
	@ManyToOne(optional = false)
	private Flight				flight;


	@Transient
	public Money getCost() {
		BookingRepository repository = SpringHelper.getBean(BookingRepository.class);

		Double flightPrice = this.flight != null ? this.flight.getCost().getAmount() : 0.0;

		String currency = this.flight != null ? this.flight.getCost().getCurrency() : "EUR";

		Integer passengerCount = repository.countPassengersByLocatorCode(this.locatorCode);
		passengerCount = passengerCount != null ? passengerCount : 0;

		Money cost = new Money();

		cost.setCurrency(currency);
		cost.setAmount(passengerCount * flightPrice);

		return cost;

	}

}
