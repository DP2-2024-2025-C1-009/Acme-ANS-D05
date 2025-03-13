
package acme.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
<<<<<<< HEAD
import javax.validation.Valid;
=======
>>>>>>> e2f00f82ed61c657e3b4ea6560f0d77b1d7812d2

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
public class Review extends AbstractEntity {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
<<<<<<< HEAD
	@ValidString(min = 1, max = 50)
=======
	@ValidString(min = 1, max = 50, message = "{acme.validation.review.alias-length}")
>>>>>>> e2f00f82ed61c657e3b4ea6560f0d77b1d7812d2
	@Automapped
	private String				alias;

	@Mandatory
<<<<<<< HEAD
	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date				moment;

	@Mandatory
	@ValidString(min = 1, max = 50)
=======
	@Temporal(TemporalType.TIMESTAMP)
	@ValidMoment(past = true, message = "{acme.validation.review.moment-past}")
	private Date				moment;

	@Mandatory
	@ValidString(min = 1, max = 50, message = "{acme.validation.review.subject-length}")
>>>>>>> e2f00f82ed61c657e3b4ea6560f0d77b1d7812d2
	@Automapped
	private String				subject;

	@Mandatory
<<<<<<< HEAD
	@ValidString(min = 1, max = 255)
=======
	@ValidString(min = 1, max = 255, message = "{acme.validation.review.text-length}")
>>>>>>> e2f00f82ed61c657e3b4ea6560f0d77b1d7812d2
	@Automapped
	private String				text;

	@Optional
<<<<<<< HEAD
	@ValidNumber(min = 0, max = 10, fraction = 2)
=======
	@ValidNumber(min = 0, max = 10, message = "{acme.validation.review.score-range}")
>>>>>>> e2f00f82ed61c657e3b4ea6560f0d77b1d7812d2
	@Automapped
	private Double				score;

	@Optional
<<<<<<< HEAD
	@Valid
	@Automapped
	private Boolean				recommended;
=======
	@Automapped
	private Boolean				isRecommended;
>>>>>>> e2f00f82ed61c657e3b4ea6560f0d77b1d7812d2
}
