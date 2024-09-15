package com.epam.wca.gym.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Constants {
    public static final String TIME_ZONED = "T00:00:00Z";
    public static final String ENTER_YOUR_FIRST_NAME = "Enter your First Name: ";
    public static final String ENTER_YOUR_LAST_NAME = "Enter your Last Name: ";
    public static final String UNAUTHORIZED_ACCESS_ATTEMPT_PLEASE_LOG_IN_FIRST =
            "Unauthorized access attempt. Please log in first.";
    public static final String ACCESS_DENIED = "Access Denied: {}";
    public static final String BACK_TO_MAIN_MENU = "0. Back to Main Menu";
    public static final String TRAINEE_NOT_FOUND = "Trainee not found";
    public static final String TRAINER_NOT_FOUND = "Trainer not found";
    public static final String PASSWORD_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*?";
    public static final int PASSWORD_LENGTH = 10;
    public static final int PASSWORD_GENERATION_START_INDEX = 0;
    public static final int SERIAL_NUMBER_INCREMENT = 1;
    public static final int INITIAL_SERIAL_NUMBER = 2;
}