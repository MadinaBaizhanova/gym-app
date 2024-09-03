package com.epam.wca.gym.utils;

import com.epam.wca.gym.entity.User;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

import static com.epam.wca.gym.utils.Constants.*;

@Slf4j
@Component
public class UsernameGenerator {
    public static String generateUsername(String firstName, String lastName, Map<Long, User> existingUsers) {
        validateName(firstName, "First Name");
        validateName(lastName, "Last Name");

        String username = (firstName.trim() + "." + lastName.trim()).toLowerCase();

        Optional<Integer> serialNumber = existingUsers.values().stream()
                .filter(user -> user.getUsername().startsWith(username))
                .map(user -> user.getUsername().replace(username, ""))
                .filter(serial -> !serial.isEmpty())
                .map(Integer::parseInt)
                .max(Integer::compareTo);

        if (serialNumber.isPresent()) {
            return username + serialNumber.get() + SERIAL_NUMBER_INCREMENT;
        } else if (existingUsers.values().stream().anyMatch(user -> user.getUsername().equals(username))) {
            return username + INITIAL_SERIAL_NUMBER;
        }

        return username;
    }

    private static void validateName(String name, String fieldName) {
        if (name == null || name.trim().isEmpty()) {
            log.error("{} is not provided. Impossible to generate the username.", fieldName);
            throw new IllegalArgumentException(fieldName + " cannot be null or empty.");
        }
    }
}