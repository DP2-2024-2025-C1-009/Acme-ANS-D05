
package acme.entities;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

import acme.client.components.basis.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Review extends AbstractEntity {

	// Serialisation identifier
	private static final long	serialVersionUID	= 1L;

	// Attributes 

	@Column(nullable = false, length = 50)
	@Size(max = 50, message = "The alias must contain at most 50 characters")
	private String				alias;

	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@Past(message = "The moment must be in the past")
	private LocalDateTime		moment;

	@Column(nullable = false, length = 50)
	@Size(max = 50, message = "The subject must contain at most 50 characters")
	private String				subject;

	@Column(nullable = false, length = 255)
	@Size(max = 255, message = "The text must contain at most 255 characters")
	private String				text;

	@Column(nullable = true)
	@DecimalMin(value = "0.0", message = "Score must be at least 0")
	@DecimalMax(value = "10.0", message = "Score must be at most 10")
	private Double				score;

	@Column(nullable = false)
	private Boolean				recommended;
}
