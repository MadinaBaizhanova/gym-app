package com.epam.wca.gym.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to monitor the execution of specific endpoints within the Gym CRM application.
 * When applied to a method, it tracks metrics such as the number of times the method is invoked
 * and the time taken for execution.
 */

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MonitorEndpoint {
    String value() default "";
}