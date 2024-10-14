package com.epam.wca.gym.utils;

import com.epam.wca.gym.dao.UserDAO;
import com.epam.wca.gym.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public final class UsernameGenerator {

    private static final int SERIAL_NUMBER_INCREMENT = 1;
    private static final int INITIAL_SERIAL_NUMBER = 2;

    private final UserDAO userDAO;

    public String generateUsername(String firstName, String lastName) {
        String baseUsername = firstName.toLowerCase() + "." + lastName.toLowerCase();
        String uniqueUsername = baseUsername;

        List<User> usersWithSimilarUsernames = userDAO.findByUsernameStartingWith(baseUsername);

        Optional<Integer> serialNumber = usersWithSimilarUsernames.stream()
                .map(user -> user.getUsername().replace(baseUsername, ""))
                .filter(serial -> !serial.isEmpty())
                .map(Integer::parseInt)
                .max(Integer::compareTo);

        if (serialNumber.isPresent()) {
            uniqueUsername = baseUsername + (serialNumber.get() + SERIAL_NUMBER_INCREMENT);
        } else if (usersWithSimilarUsernames.stream().anyMatch(user -> user.getUsername().equals(baseUsername))) {
            uniqueUsername = baseUsername + INITIAL_SERIAL_NUMBER;
        }

        return uniqueUsername;
    }
}