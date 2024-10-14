package com.epam.wca.gym.actuator;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@Profile({"local", "dev"})
@RequiredArgsConstructor
public class H2TrainingTypeHealthCheck extends AbstractHealthIndicator {

    private final JdbcTemplate jdbcTemplate;

    @Override
    protected void doHealthCheck(Health.Builder builder) {
        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM training_type", Integer.class);

        if (count != null && count > 0) {
            builder.up().withDetail("message", "Training types data loaded successfully!");
        } else {
            builder.down().withDetail("message", "No data found in the training_type table");
        }
    }
}