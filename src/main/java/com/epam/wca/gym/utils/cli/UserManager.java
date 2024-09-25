package com.epam.wca.gym.utils.cli;

import com.epam.wca.gym.dto.TraineeDTO;
import com.epam.wca.gym.dto.TrainerDTO;
import com.epam.wca.gym.entity.Role;
import com.epam.wca.gym.exception.InvalidInputException;
import com.epam.wca.gym.facade.GymFacade;
import com.epam.wca.gym.service.SecurityService;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

import static com.epam.wca.gym.utils.Constants.ENTER_YOUR_FIRST_NAME;
import static com.epam.wca.gym.utils.Constants.ENTER_YOUR_LAST_NAME;
import static com.epam.wca.gym.utils.Constants.TIME_ZONED;

/**
 * @deprecated
 * <p>
 * This class previously served as a helper class for the GymApplication class.
 * </p>
 * Due to the new, RESTful, version of the application, the responsibilities of this class have been moved
 * to {@link com.epam.wca.gym.controller.UserController}
 */

@Slf4j
@UtilityClass
@Deprecated(since = "1.2")
public class UserManager {

    public static void register(Scanner scanner, GymFacade gymFacade) {
        boolean backToMain = false;
        while (!backToMain) {
            MenuUtils.displayMenu("\nRegister as:", "1. Trainee", "2. Trainer", "0. Back to Main Menu");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> registerTrainee(scanner, gymFacade);
                case "2" -> registerTrainer(scanner, gymFacade);
                case "0" -> backToMain = true;
                default -> MenuUtils.invalidChoice();
            }
        }
    }

    public static void login(Scanner scanner, GymFacade gymFacade, SecurityService securityService) {
        log.info("Enter Username: ");
        String username = scanner.nextLine();

        log.info("Enter Password: ");
        String password = scanner.nextLine();

        Role role = gymFacade.user().authenticate(username, password);

        if (role == Role.TRAINEE) {
            log.info("Login successful as Trainee!");
            securityService.login(username, Role.TRAINEE);
        } else if (role == Role.TRAINER) {
            log.info("Login successful as Trainer!");
            securityService.login(username, Role.TRAINER);
        } else {
            log.info("Login failed. Invalid username or password.");
        }
    }

    public static void showUserManagement(Scanner scanner, GymFacade gymFacade, SecurityService securityService) {
        boolean backToMain = false;
        while (!backToMain) {
            MenuUtils.displayMenu(
                    "\nGeneral User Management:", "1. Activate Profile", "2. Deactivate Profile",
                    "3. Change Password", "0. Back to Main Menu");

            try {
                String choice = scanner.nextLine();

                switch (choice) {
                    case "1" -> activateUser(gymFacade, securityService);
                    case "2" -> deactivateUser(gymFacade, securityService);
                    case "3" -> changePassword(scanner, gymFacade, securityService);
                    case "0" -> backToMain = true;
                    default -> MenuUtils.invalidChoice();
                }
            } catch (SecurityException e) {
                log.error("Access Denied: {}", e.getMessage());
            }
        }
    }

    public static void logout(SecurityService securityService) {
        if (securityService.isAuthenticated()) {
            String username = securityService.getAuthenticatedUsername();
            securityService.logout();
            log.info("User {} has logged out successfully.", username);
        } else {
            log.warn("No user is currently logged in.");
        }
    }

    private void registerTrainee(Scanner scanner, GymFacade gymFacade) {
        log.info(ENTER_YOUR_FIRST_NAME);
        String firstName = scanner.nextLine();

        log.info(ENTER_YOUR_LAST_NAME);
        String lastName = scanner.nextLine();

        ZonedDateTime dateOfBirth = null;
        log.info("Enter Date of Birth (format: yyyy-mm-dd) (optional): ");
        String dob = scanner.nextLine();
        if (!dob.isBlank()) {
            try {
                dateOfBirth = ZonedDateTime.parse(dob + TIME_ZONED);
            } catch (DateTimeParseException e) {
                log.error("Invalid Date of Birth. Please enter a valid date in yyyy-mm-dd format.");
                return;
            }
        }

        log.info("Enter Address (optional): ");
        String address = scanner.nextLine();

        TraineeDTO traineeDTO = new TraineeDTO(null, firstName, lastName, null, dateOfBirth, address,
                true, null);

        try {
            gymFacade.trainee().create(traineeDTO);
        } catch (Exception e) {
            log.error("Error registering Trainee: {}", e.getMessage());
        }
    }

    private void registerTrainer(Scanner scanner, GymFacade gymFacade) {
        log.info(ENTER_YOUR_FIRST_NAME);
        String firstName = scanner.nextLine();

        log.info(ENTER_YOUR_LAST_NAME);
        String lastName = scanner.nextLine();

        log.info("Enter Training Type (available types: FITNESS, YOGA, ZUMBA, STRETCHING, CARDIO, CROSSFIT): ");
        String trainingType = scanner.nextLine();

        TrainerDTO trainerDTO = new TrainerDTO(null, firstName, lastName, null,
                trainingType, true, null);

        try {
            gymFacade.trainer().create(trainerDTO);
        } catch (Exception e) {
            log.error("Error registering Trainer: {}", e.getMessage());
        }
    }

    private void activateUser(GymFacade gymFacade, SecurityService securityService) {
        String username = securityService.getAuthenticatedUsername();
        gymFacade.user().activateUser(username);
        log.info("User {} activated successfully.", username);
    }

    private void deactivateUser(GymFacade gymFacade, SecurityService securityService) {
        String username = securityService.getAuthenticatedUsername();
        gymFacade.user().deactivateUser(username);
        log.info("User {} deactivated successfully.", username);
    }

    private void changePassword(Scanner scanner, GymFacade gymFacade, SecurityService securityService) {
        String username = securityService.getAuthenticatedUsername();

        log.info("Enter your current password: ");
        String currentPassword = scanner.nextLine();

        log.info("Enter new password: ");
        String newPassword = scanner.nextLine();

        try {
            gymFacade.user().changePassword(username, currentPassword, newPassword);
        } catch (InvalidInputException e) {
            log.error("Error changing password: {}", e.getMessage());
        }
    }
}