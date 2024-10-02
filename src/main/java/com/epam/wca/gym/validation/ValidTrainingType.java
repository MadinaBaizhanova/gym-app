package com.epam.wca.gym.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = TrainingTypeValidator.class)
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidTrainingType {

    String message() default "Invalid training type. Please provide one of the following training types: " +
                             "Fitness, Yoga, Zumba, Stretching, Cardio, or Crossfit.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}