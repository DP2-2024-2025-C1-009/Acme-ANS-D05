
package acme.entities.maintenance;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import acme.client.components.basis.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class MaintenanceRecord extends AbstractEntity {

	// Serialisation identifier -----------------------------------------------
	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@NotBlank
	@Temporal(TemporalType.TIMESTAMP)
	private Date				moment;

	@NotBlank
	@Enumerated(EnumType.STRING)
	private Status				status;

	@NotBlank
	@Temporal(TemporalType.TIMESTAMP)
	private Date				nextInspectionDueDate;

	@NotBlank
	@Min(0)
	private double				estimatedCost;

	@Length(max = 255)
	private String				notes;


	public enum Status {
		PENDING, IN_PROGRESS, COMPLETED
	}


	// Relationships ----------------------------------------------------------
	@NotNull
	@Valid
	@ManyToOne(optional = false)
	private Task task;
}
