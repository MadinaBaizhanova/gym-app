package com.epam.wca.gym.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is used to indicate that a method requires the user to be authenticated before execution.
 * Apply this annotation to methods that should only be executed by authenticated users.
 * The SecurityAspect will handle the authentication check, and if the user is not authenticated,
 * the SecurityException will be thrown, preventing further execution.
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Secured {
}