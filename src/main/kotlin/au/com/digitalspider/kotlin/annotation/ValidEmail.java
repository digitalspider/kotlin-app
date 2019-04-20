package au.com.digitalspider.kotlin.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Pattern;

@Target({ElementType.FIELD, ElementType.METHOD}) 
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {})
@Pattern(regexp = ValidEmail.EMAIL_PATTERN, flags = Pattern.Flag.CASE_INSENSITIVE, message="Invalid email")
@Documented
public @interface ValidEmail {   
	public static final String EMAIL_PATTERN = "^[_A-Za-z0-9-+]+(.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(.[A-Za-z0-9]+)*(.[A-Za-z]{2,})$";

    String message() default "Invalid email";
    Class<?>[] groups() default {}; 
    Class<? extends Payload>[] payload() default {};
}