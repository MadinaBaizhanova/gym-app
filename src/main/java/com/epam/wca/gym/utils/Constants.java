package com.epam.wca.gym.utils;

public class Constants {

    private Constants() {
        throw new IllegalStateException("This is a Utility class and cannot be instantiated");
    }

    public static final String PASSWORD_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*?";
    public static final String TRAINEE_WITH_ID_NOT_FOUND = "Trainee with ID: {} not found.";
    public static final String INVALID_TRAINEE_ID_PROVIDED = "Invalid trainee ID provided: {}";
    public static final long ID_DEFAULT_VALUE = 0L;
    public static final int ID_INCREMENT = 1;
    public static final int INITIAL_SERIAL_NUMBER = 2;
    public static final int SERIAL_NUMBER_INCREMENT = 1;
    public static final int PASSWORD_LENGTH = 10;
    public static final int PASSWORD_GENERATION_START_INDEX = 0;
    public static final String LINE_SPLIT = ",";
    public static final int COLUMN_1 = 0;
    public static final int COLUMN_2 = 1;
    public static final int COLUMN_3 = 2;
    public static final int COLUMN_4 = 3;
    public static final int COLUMN_5 = 4;
    public static final int COLUMN_6 = 5;
    public static final int COLUMN_7 = 6;
    public static final String ENTER_YOUR_CHOICE = "Enter your choice: ";
    public static final String INVALID_CHOICE_PLEASE_TRY_AGAIN = "Invalid choice. Please try again.";
    public static final String RETURNING_TO_MAIN_MENU = "Returning to Main Menu...";
    public static final String CHOICE_1 = "1";
    public static final String CHOICE_2 = "2";
    public static final String CHOICE_3 = "3";
    public static final String CHOICE_4 = "4";
    public static final String CHOICE_5 = "5";
    public static final String CHOICE_6 = "6";
    public static final String EXITING = "Exiting...";
    public static final String ENTER_TRAINING_TYPE_AVAILABLE_TYPES_FITNESS_YOGA_ZUMBA_STRETCHING_CARDIO_CROSSFIT = "Enter Training Type (available types: FITNESS, YOGA, ZUMBA, STRETCHING, CARDIO, CROSSFIT): ";
}