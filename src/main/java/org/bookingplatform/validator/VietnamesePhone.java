package org.bookingplatform.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = VietnamesePhoneValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface VietnamesePhone {
    String message() default "Invalid Vietnamese phone number";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}