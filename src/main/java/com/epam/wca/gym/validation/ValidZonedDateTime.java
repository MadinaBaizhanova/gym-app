package com.epam.wca.gym.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ZonedDateTimeValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidZonedDateTime {

    String message() default "Invalid date of birth. Expected ZonedDateTime format, e.g., '2000-01-01T00:00:00Z'.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}