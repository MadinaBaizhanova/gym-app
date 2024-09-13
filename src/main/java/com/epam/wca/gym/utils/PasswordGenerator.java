package com.epam.wca.gym.utils;

import lombok.experimental.UtilityClass;

import java.util.Random;

import static com.epam.wca.gym.utils.Constants.PASSWORD_CHARACTERS;
import static com.epam.wca.gym.utils.Constants.PASSWORD_GENERATION_START_INDEX;
import static com.epam.wca.gym.utils.Constants.PASSWORD_LENGTH;

@UtilityClass
public final class PasswordGenerator {

    private static final Random random = new Random();

    public static String generatePassword() {
        StringBuilder password = new StringBuilder(PASSWORD_LENGTH);

        for (int i = PASSWORD_GENERATION_START_INDEX; i < PASSWORD_LENGTH; i++) {
            password.append(PASSWORD_CHARACTERS.charAt(random.nextInt(PASSWORD_CHARACTERS.length())));
        }

        return password.toString();
    }
}