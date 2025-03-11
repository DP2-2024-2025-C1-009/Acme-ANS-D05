
package acme.entities.maintenance;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;

import acme.client.components.basis.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Task extends AbstractEntity {

	// Serialisation identifier -----------------------------------------------
	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@NotBlank
	@Enumerated(EnumType.STRING)
	private TaskType			type;

	@NotBlank
	@Length(max = 255)
	@Column(length = 255)
	private String				description;

	@NotBlank
	@Min(0)
	@Max(10)
	private int					priority;

	@NotBlank
	@DecimalMin("0.0")
	private double				estimatedDuration;


	public enum TaskType {
		MAINTENANCE, INSPECTION, REPAIR, SYSTEM_CHECK
	}
}
