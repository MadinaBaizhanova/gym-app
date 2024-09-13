package com.epam.wca.gym.utils;

import com.epam.wca.gym.entity.User;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Optional;

import static com.epam.wca.gym.utils.Constants.INITIAL_SERIAL_NUMBER;
import static com.epam.wca.gym.utils.Constants.SERIAL_NUMBER_INCREMENT;

@Slf4j
@UtilityClass
public final class UsernameGenerator {
    public static String generateUsername(String firstName, String lastName, Map<Long, User> existingUsers) {
        validateName(firstName, "First Name");
        validateName(lastName, "Last Name");

        String baseUsername = firstName.toLowerCase() + "." + lastName.toLowerCase();
        String uniqueUsername = baseUsername;

        Optional<Integer> serialNumber = existingUsers.values().stream()
                .filter(user -> user.getUsername().startsWith(baseUsername))
                .map(user -> user.getUsername().replace(baseUsername, ""))
                .filter(serial -> !serial.isEmpty())
                .map(Integer::parseInt)
                .max(Integer::compareTo);

        if (serialNumber.isPresent()) {
            uniqueUsername = baseUsername + (serialNumber.get() + SERIAL_NUMBER_INCREMENT);
        } else if (existingUsers.values().stream().anyMatch(user -> user.getUsername().equals(baseUsername))) {
            uniqueUsername = baseUsername + INITIAL_SERIAL_NUMBER;
        }

        return uniqueUsername;
    }

    private static void validateName(String name, String fieldName) {
        if (name == null || name.trim().isEmpty()) {
            log.error("{} is not provided. Impossible to generate the username.", fieldName);
            throw new IllegalArgumentException(fieldName + " cannot be null or empty.");
        }
    }
}