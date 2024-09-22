package com.epam.wca.gym.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is used to indicate that a method should only be executed if the currently logged-in
 * trainee is active (the 'isActive' field equals true for this trainee). Apply this annotation to methods
 * that require an active trainee before execution. The ActiveTraineeAspect verifies the trainee's active
 * status and throws the IllegalStateException if the trainee is inactive.
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckActiveTrainee {
}