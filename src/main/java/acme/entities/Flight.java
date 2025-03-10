
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
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Flight extends AbstractEntity {

	@Column(nullable = false, length = 50)
	private String		tag;

	@Column(nullable = false)
	private boolean		selfTransfer;

	@Column(nullable = false)
	@Min(value = 0, message = "Cost must be at least 0")
	private double		cost;

	@Column(nullable = true, length = 255)
	private String		description;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	private Date		scheduledDeparture;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	private Date		scheduledArrival;

	@Column(nullable = false, length = 100)
	private String		originCity;

	@Column(nullable = false, length = 100)
	private String		destinationCity;

	@Column(nullable = false)
	@Min(value = 0, message = "Number of layovers must be at least 0")
	private int			numberOfLayovers;

	@OneToMany(mappedBy = "flight", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Leg>	legs;
}
