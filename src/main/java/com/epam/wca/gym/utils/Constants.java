package com.epam.wca.gym.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Constants {
    public static final String MISSING_TRAINEE_TEMPLATE = "Trainee not found with username: %s";
    public static final String MISSING_TRAINER_TEMPLATE = "Trainer not found with username: %s";
    public static final String MISSING_TRAINING_TEMPLATE = "Training not found with name: %s";
    public static final String PASSWORD_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*?";
    public static final int PASSWORD_LENGTH = 10;
    public static final int PASSWORD_GENERATION_START_INDEX = 0;
    public static final int SERIAL_NUMBER_INCREMENT = 1;
    public static final int INITIAL_SERIAL_NUMBER = 2;
    public static final String USERNAME = "username";
    public static final int ALLOCATION_SIZE = 1;
    public static final int FIRST_NAME_MIN_SIZE = 3;
    public static final int LAST_NAME_MIN_SIZE = 3;
    public static final int DURATION_MIN_SIZE = 30;
    public static final int PASSWORD_MIN_SIZE = 10;
    public static final int LIMIT = 2;
    public static final int BASE64_USERNAME = 0;
    public static final int BASE64_PASSWORD = 1;
    public static final String TRANSACTION_ID_HEADER = "transactionId";
}