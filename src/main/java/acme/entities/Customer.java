
package acme.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.validation.Optional;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Customer extends AbstractEntity {

	@Size(min = 8, max = 9)
	@Column(nullable = false)
	@Pattern(regexp = "^[A-Z]{2-3}\\d{6}$", message = "Customer Identificator " + "must have 2/3 letters followed by 6 numbers")
	private String	customerId;

	@Size(min = 6, max = 15)
	@Column(nullable = false)
	@Pattern(regexp = "^+?\\d{6,15}$", message = "Customer telephone numbre " + "can start with simbol + and must have between 6 and 15 digits")
	private Integer	telephoneNumber;

	@Size(max = 255)
	@Column(nullable = false)
	private String	address;

	@Size(max = 50)
	@Column(nullable = false)
	private String	city;

	@Size(max = 50)
	@Column(nullable = false)
	private String	country;

	@Optional
	@Size(max = 500000)
	@Column(nullable = true)
	private Integer	accumulatedPoints;

}
