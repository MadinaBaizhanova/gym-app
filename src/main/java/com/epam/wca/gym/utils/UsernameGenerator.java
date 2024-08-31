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
            log.error(FIRST_NAME_IS_NOT_PROVIDED_IMPOSSIBLE_TO_GENERATE_THE_USERNAME_LOG);
            throw new IllegalArgumentException(FIRST_NAME_CANNOT_BE_NULL_OR_EMPTY_EXCEPTION);
        }

        if (lastName == null || lastName.trim().isEmpty()) {
            log.error(LAST_NAME_IS_NOT_PROVIDED_IMPOSSIBLE_TO_GENERATE_THE_USERNAME_LOG);
            throw new IllegalArgumentException(LAST_NAME_CANNOT_BE_NULL_OR_EMPTY_EXCEPTION);
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