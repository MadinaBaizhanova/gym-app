package com.epam.wca.gym.actuator;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TrainingTypeHealthIndicator extends AbstractHealthIndicator {
    private static final String TRAINING_TYPE_SERVICE = "Training Type Service (custom Health Indicator)";

    private final RestTemplate restTemplate;

    @Override
    protected void doHealthCheck(Health.Builder builder) {
        String url = "http://localhost:8080/gym-app/api/v1/types";
        try {
            List<?> response = restTemplate.getForObject(url, List.class);
            if (response != null && !response.isEmpty()) {
                builder.up().withDetail(TRAINING_TYPE_SERVICE, "AVAILABLE");
            } else {
                builder.down().withDetail(TRAINING_TYPE_SERVICE, "NO DATA AVAILABLE");
            }
        } catch (Exception exception) {
            builder.down(exception).withDetail(TRAINING_TYPE_SERVICE, "UNAVAILABLE");
        }
    }
}