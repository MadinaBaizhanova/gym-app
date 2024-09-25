package com.epam.wca.gym.utils.cli;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * @deprecated
 * <p>
 * This class previously served as a helper class for the GymApplication class.
 * </p>
 * Due to the new, RESTful, version of the application, this class is no longer needed.
 */

@Slf4j
@UtilityClass
@Deprecated(since = "1.2")
public class MenuUtils {
    public static void displayMenu(String... menuOptions) {
        for (String option : menuOptions) {
            log.info(option);
        }
    }

    public static void invalidChoice() {
        log.warn("Invalid choice! Please try again.");
    }
}