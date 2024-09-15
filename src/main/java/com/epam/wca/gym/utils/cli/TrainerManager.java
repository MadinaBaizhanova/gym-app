package com.epam.wca.gym.utils.cli;

import com.epam.wca.gym.dto.TrainerDTO;
import com.epam.wca.gym.dto.TrainingDTO;
import com.epam.wca.gym.facade.GymFacade;
import com.epam.wca.gym.service.SecurityService;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import static com.epam.wca.gym.utils.Constants.ACCESS_DENIED;
import static com.epam.wca.gym.utils.Constants.BACK_TO_MAIN_MENU;
import static com.epam.wca.gym.utils.Constants.TIME_ZONED;

@Slf4j
@UtilityClass
public class TrainerManager {

    public static void showTrainerManagement(Scanner scanner, GymFacade gymFacade, SecurityService securityService) {
        if (securityService.isTrainee()) {
            log.error("Access denied: you are not a trainer and cannot access this menu.");
            return;
        }

        boolean backToMain = false;
        while (!backToMain) {
            MenuUtils.displayMenu("\nTrainer Management:", "1. View my Trainer Account information",
                    "2. Update my profile", "3. View my scheduled Trainings", BACK_TO_MAIN_MENU);

            try {
                String choice = scanner.nextLine();

                switch (choice) {
                    case "1" -> viewProfile(gymFacade, securityService);
                    case "2" -> updateProfile(scanner, gymFacade, securityService);
                    case "3" -> viewTrainings(scanner, gymFacade, securityService);
                    case "0" -> backToMain = true;
                    default -> MenuUtils.invalidChoice();
                }
            } catch (SecurityException e) {
                log.error(ACCESS_DENIED, e.getMessage());
            }
        }
    }

    private static void viewProfile(GymFacade gymFacade, SecurityService securityService) {
        String username = securityService.getAuthenticatedUsername();
        try {
            Optional<TrainerDTO> trainer = gymFacade.trainer().findByUsername(username);
            log.info("Trainer Profile: {}", trainer);
        } catch (Exception e) {
            log.error("Error finding trainer: {}", e.getMessage());
        }
    }

    private static void updateProfile(Scanner scanner, GymFacade gymFacade, SecurityService securityService) {
        String username = securityService.getAuthenticatedUsername();

        log.info("Enter new First Name (leave blank to keep unchanged): ");
        String firstName = scanner.nextLine();

        log.info("Enter new Last Name (leave blank to keep unchanged): ");
        String lastName = scanner.nextLine();

        log.info("Enter new Training Type (leave blank to keep unchanged): ");
        String trainingType = scanner.nextLine();

        TrainerDTO updatedDTO = new TrainerDTO(null, firstName, lastName, username, trainingType, true);
        try {
            gymFacade.trainer().update(updatedDTO);
        } catch (Exception e) {
            log.error("Error updating trainer profile: {}", e.getMessage());
        }
    }

    private static void viewTrainings(Scanner scanner, GymFacade gymFacade, SecurityService securityService) {
        String trainerUsername = securityService.getAuthenticatedUsername();

        log.info("Enter optional Trainee First Name or Last Name (or leave blank): ");
        String traineeName = scanner.nextLine().trim();

        ZonedDateTime fromDate = null;
        log.info("Enter optional 'From Date' (format: yyyy-mm-dd) or leave blank: ");
        String from = scanner.nextLine().trim();
        if (!from.isEmpty()) {
            try {
                fromDate = ZonedDateTime.parse(from + TIME_ZONED);
            } catch (DateTimeParseException e) {
                log.error("Invalid 'From Date' format. Please enter a valid date in yyyy-mm-dd format.");
                return;
            }
        }

        ZonedDateTime toDate = null;
        log.info("Enter optional 'To Date' (format: yyyy-mm-dd) or leave blank: ");
        String to = scanner.nextLine().trim();
        if (!to.isEmpty()) {
            try {
                toDate = ZonedDateTime.parse(to + TIME_ZONED);
            } catch (DateTimeParseException e) {
                log.error("Invalid 'To Date' format. Please enter a valid date in yyyy-mm-dd format.");
                return;
            }
        }

        List<TrainingDTO> trainings = gymFacade.trainer().findTrainings(trainerUsername, traineeName, fromDate, toDate);
        if (trainings.isEmpty()) {
            log.info("No trainings found.");
        } else {
            trainings.forEach(training -> log.info(training.toString()));
        }
    }
}