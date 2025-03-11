
package acme.entities;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidMoney;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Flight extends AbstractEntity {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Column(nullable = false, length = 50)
	private String				tag;

	@Mandatory
	@Automapped
	private boolean				selfTransfer;

	@Column(nullable = false)
	@ValidMoney(min = 0)
	private Money				cost;

	@Column(nullable = true, length = 255)
	private String				description;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	private Date				scheduledDeparture;

	// Los vuelos son gestionados por un manager
	// Hay que crear un repositorio // FlightRepository extends AbstractRepository

	// ATRIBUTOS DERIVADOS DE LEG con transient CAMBIAR

	//	@Transient
	//	public String getArrivalCity() {
	//		String result;
	//		FlightRepository repository;
	//
	//		repository = SpringHelper.getBean(FlighRepository.class);
	//		result = repository.computedArrivalCityByFlight(this.getId());
	//
	//	}

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	private Date				scheduledArrival;

	@Column(nullable = false, length = 100)
	private String				originCity;

	@Column(nullable = false, length = 100)
	private String				destinationCity;

	@Column(nullable = false)
	@Min(value = 0, message = "Number of layovers must be at least 0")
	private int					numberOfLayovers;

	@OneToMany(mappedBy = "flight", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Leg>			legs;
}
