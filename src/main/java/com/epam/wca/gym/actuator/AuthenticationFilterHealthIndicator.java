package com.epam.wca.gym.actuator;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthenticationFilterHealthIndicator extends AbstractHealthIndicator {

    private static final String AUTH_FILTER_SERVICE = "Authentication Filter Service (custom Health Indicator)";

    private final ApplicationContext applicationContext;

    @Override
    protected void doHealthCheck(Health.Builder builder) {
        try {
            if (applicationContext.containsBean("authenticationFilter")) {
                builder.up().withDetail(AUTH_FILTER_SERVICE, "AUTHENTICATION FILTER AVAILABLE");
            } else {
                builder.down().withDetail(AUTH_FILTER_SERVICE, "AUTHENTICATION FILTER NOT FOUND");
            }
        } catch (Exception e) {
            builder.down(e).withDetail(AUTH_FILTER_SERVICE, "ERROR CHECKING AUTHENTICATION FILTER");
        }
    }
}