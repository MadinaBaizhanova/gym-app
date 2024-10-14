package com.epam.wca.gym.utils;

import lombok.experimental.UtilityClass;

import java.security.SecureRandom;

@UtilityClass
public final class PasswordGenerator {

    private static final String PASSWORD_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*?";
    private static final int PASSWORD_LENGTH = 10;

    private static final SecureRandom random = new SecureRandom();

    public static String generatePassword() {
        StringBuilder password = new StringBuilder(PASSWORD_LENGTH);

        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            password.append(PASSWORD_CHARACTERS.charAt(random.nextInt(PASSWORD_CHARACTERS.length())));
        }

        return password.toString();
    }
}