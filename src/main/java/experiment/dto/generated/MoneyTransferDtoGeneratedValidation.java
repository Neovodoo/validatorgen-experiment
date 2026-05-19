package experiment.dto.generated;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Target(TYPE)
@Retention(RUNTIME)
@Constraint(validatedBy = MoneyTransferDtoGeneratedConstraintValidator.class)
public @interface MoneyTransferDtoGeneratedValidation {
    String message() default "MoneyTransferDto generated validation failed";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}