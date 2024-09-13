package com.epam.wca.gym.utils.cli;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public class MenuUtils {
    public static void displayMenu(String... menuOptions) {
        for (String option : menuOptions) {
            log.info(option);
        }
    }
}