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
        if (firstName == null || firstName.trim().isEmpty()) {
            log.error("First Name is not provided. Impossible to generate the username.");
            throw new IllegalArgumentException("First Name cannot be null or empty.");
        }

        if (lastName == null || lastName.trim().isEmpty()) {
            log.error("Last Name is not provided. Impossible to generate the username.");
            throw new IllegalArgumentException("Last Name cannot be null or empty.");
        }

        String baseUsername = firstName.toLowerCase() + USERNAME_DOT_SEPARATOR + lastName.toLowerCase();
        String uniqueUsername = baseUsername;

        Optional<Integer> serialNumber = existingUsers.values().stream()
                .filter(user -> user.getUsername().startsWith(baseUsername))
                .map(user -> user.getUsername().replace(baseUsername, SERIAL_PART_EXTRACTION))
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
}