package com.epam.wca.gym.actuator;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Component
@RequiredArgsConstructor
public class DatabaseHealthIndicator extends AbstractHealthIndicator {

    private static final String DATABASE = "database (custom Health Indicator)";
    private static final String POSTGRE_SQL = "PostgreSQL";
    private static final int TIMEOUT = 1;

    private final DataSource dataSource;

    @Override
    protected void doHealthCheck(Health.Builder builder) {
        try (Connection connection = dataSource.getConnection()) {
            if (connection.isValid(TIMEOUT)) {
                builder.up().withDetail(DATABASE, POSTGRE_SQL).withDetail("status", "CONNECTED");
            } else {
                builder.down().withDetail(DATABASE, POSTGRE_SQL).withDetail("status", "NOT CONNECTED");
            }
        } catch (SQLException exception) {
            builder.down().withDetail(DATABASE, POSTGRE_SQL).withDetail("error", exception.getMessage());
        }
    }
}