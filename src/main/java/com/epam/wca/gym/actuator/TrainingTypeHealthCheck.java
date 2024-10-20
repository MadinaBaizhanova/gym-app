package com.epam.wca.gym.actuator;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
@Profile("prod")
@RequiredArgsConstructor
public class TrainingTypeHealthCheck extends AbstractHealthIndicator {

    private static final String TRAINING_TYPE_SERVICE = "Training Type Service (custom Health Indicator)";

    private final RestTemplate restTemplate;

    @Value("${training.type.service.url}")
    private String trainingTypeServiceUrl;

    @Override
    protected void doHealthCheck(Health.Builder builder) {
        try {
            List<?> response = restTemplate.getForObject(trainingTypeServiceUrl, List.class);
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