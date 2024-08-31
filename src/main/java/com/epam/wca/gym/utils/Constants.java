package com.epam.wca.gym.utils;

public class Constants {

    private Constants() {
        throw new IllegalStateException(UTILITY_CLASS_INSTANTIATION_EXCEPTION);
    }

    public static final String UTILITY_CLASS_INSTANTIATION_EXCEPTION = "This is a Utility class and cannot be instantiated";
    public static final String PASSWORD_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*?";

    public static final String USER_CREATED_WITH_ID = "User created with ID: {}";
    public static final String USER_WITH_ID_NOT_FOUND = "User with ID not found: ";
    public static final String USER_WITH_ID_NOT_FOUND_LOG = "User with ID: {} not found.";
    public static final String USER_WITH_ID_HAS_BEEN_DEACTIVATED = "User with ID: {} has been deactivated.";
    public static final String USER_CREATION_FAILED_DUE_TO_INVALID_INPUT = "User creation failed due to invalid input: {}";

    public static final String TRAINEE_WITH_ID_NOT_FOUND = "Trainee with ID: {} not found.";
    public static final String TRAINEE_CREATED_WITH_ID = "Trainee created with ID: {}";
    public static final String TRAINEE_WITH_ID_UPDATED_WITH_NEW_ADDRESS = "Trainee with ID: {} updated with new address: {}";
    public static final String TRAINEE_WITH_ID_HAS_BEEN_DELETED = "Trainee with ID: {} has been deleted.";

    public static final String TRAINER_WITH_ID_NOT_FOUND_LOG = "Trainer with ID: {} not found.";
    public static final String TRAINER_CREATED_WITH_ID = "Trainer created with ID: {}";
    public static final String TRAINER_WITH_ID_UPDATED_WITH_NEW_TRAINING_TYPE = "Trainer with ID: {} updated with new training type: {}";
    public static final String INVALID_TRAINING_TYPE_PROVIDED = "Invalid training type provided: {}";
    public static final String INVALID_TRAINER_ID_PROVIDED = "Invalid trainer ID provided: {}";

    public static final String TRAINING_SESSION_CREATED_WITH_ID = "Training session created with ID: {}";
    public static final String TRAINING_CREATION_FAILED = "Trainee ID: {} or Trainer ID: {} not found. Training creation failed.";
    public static final String INVALID_NUMBER_FORMAT_FOR_TRAINEE_ID_TRAINER_ID_OR_DURATION = "Invalid number format for trainee Id, trainer Id, or duration: {}";
    public static final String INVALID_DATE_FORMAT_PROVIDED_FOR_TRAINING_DATE = "Invalid date format provided for training date: {}";
    public static final String INVALID_TRAINING_TYPE_PROVIDED1 = "Invalid training type provided: {}";
    public static final String INVALID_TRAINING_ID_PROVIDED = "Invalid training ID provided: {}";
    public static final String TRAINING_WITH_ID_NOT_FOUND = "Training with ID {} not found.";

    public static final String INVALID_DATE_FORMAT_PROVIDED_FOR_DATE_OF_BIRTH = "Invalid date format provided for date of birth: {}";
    public static final String INVALID_TRAINEE_ID_PROVIDED = "Invalid trainee ID provided: {}";

    public static final long ID_DEFAULT_VALUE = 0L;
    public static final int ID_INCREMENT = 1;

    public static final String FAILED_TO_LOAD_DATA = "Failed to load data from file: {}. Error: {}";
    public static final String INITIALIZING_STORAGES_WITH_DATA = "Initializing storages with data...";
    public static final String STORAGES_INITIALIZED_SUCCESSFULLY = "Storages initialized successfully.";

    public static final String FIRST_NAME_IS_NOT_PROVIDED_IMPOSSIBLE_TO_GENERATE_THE_USERNAME_LOG = "First Name is not provided. Impossible to generate the username.";
    public static final String LAST_NAME_IS_NOT_PROVIDED_IMPOSSIBLE_TO_GENERATE_THE_USERNAME_LOG = "Last Name is not provided. Impossible to generate the username.";
    public static final String FIRST_NAME_CANNOT_BE_NULL_OR_EMPTY_EXCEPTION = "First Name cannot be null or empty.";
    public static final String LAST_NAME_CANNOT_BE_NULL_OR_EMPTY_EXCEPTION = "Last Name cannot be null or empty.";
    public static final int INITIAL_SERIAL_NUMBER = 2;
    public static final int SERIAL_NUMBER_INCREMENT = 1;
    public static final String USERNAME_DOT_SEPARATOR = ".";
    public static final String SERIAL_PART_EXTRACTION = "";
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
    public static final String EXITING = "Exiting...";
    public static final String CHOICE_6 = "6";
    public static final String ENTER_TRAINING_TYPE_AVAILABLE_TYPES_FITNESS_YOGA_ZUMBA_STRETCHING_CARDIO_CROSSFIT = "Enter Training Type (available types: FITNESS, YOGA, ZUMBA, STRETCHING, CARDIO,CROSSFIT): ";
}