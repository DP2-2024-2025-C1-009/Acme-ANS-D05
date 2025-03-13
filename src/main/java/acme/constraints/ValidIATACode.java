
package acme.constraints;

<<<<<<< HEAD
=======
import java.lang.annotation.Documented;
>>>>>>> e2f00f82ed61c657e3b4ea6560f0d77b1d7812d2
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;
<<<<<<< HEAD
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {})
@ReportAsSingleViolation

@NotBlank
@Pattern(regexp = "^[A-Z]{3}$")
public @interface ValidIATACode {

	String message() default "IATA code must be exactly three uppercase letters";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};

=======

@Documented
@Constraint(validatedBy = IATACodeValidator.class)
@Target({
	ElementType.FIELD, ElementType.METHOD, ElementType.ANNOTATION_TYPE
})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidIATACode {

	String message() default "";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
>>>>>>> e2f00f82ed61c657e3b4ea6560f0d77b1d7812d2
}
