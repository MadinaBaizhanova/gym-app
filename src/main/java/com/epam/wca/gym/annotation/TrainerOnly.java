package com.epam.wca.gym.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is used to restrict access to certain methods such that only authenticated users
 * with a "Trainer" role can execute them. Apply this annotation to methods that should only be accessible
 * to users with the "Trainer" role. The RoleAspect will handle the role validation and throw the SecurityException
 * if the user does not have the required role.
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TrainerOnly {
}