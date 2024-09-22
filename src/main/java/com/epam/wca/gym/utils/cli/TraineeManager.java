package com.epam.wca.gym.utils.cli;

import com.epam.wca.gym.dto.TraineeDTO;
import com.epam.wca.gym.dto.TrainerDTO;
import com.epam.wca.gym.dto.TrainingDTO;
import com.epam.wca.gym.entity.Training;
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
import static com.epam.wca.gym.utils.Constants.TRAINING_DURATION_INITIALIZATION;

@Slf4j
@UtilityClass
public class TraineeManager {

    public static void showTraineeManagement(Scanner scanner, GymFacade gymFacade, SecurityService securityService) {
        if (securityService.isTrainer()) {
            log.error("Access denied: you are not a trainee and cannot access this menu.");
            return;
        }

        boolean backToMain = false;
        while (!backToMain) {
            MenuUtils.displayMenu(
                    "\nTrainee Management:",
                    "1. View my Trainee Account information",
                    "2. Update my profile",
                    "3. DANGER: Delete my profile",
                    "4. View all available Trainers",
                    "5. View my Favourite Trainers",
                    "6. Add a Trainer to my Favourite Trainers list",
                    "7. Remove a Trainer from my Favourite Trainers list",
                    "8. View my scheduled Trainings",
                    "9. Schedule a Training",
                    BACK_TO_MAIN_MENU
            );

            try {
                String choice = scanner.nextLine();

                switch (choice) {
                    case "1" -> viewProfile(gymFacade, securityService);
                    case "2" -> updateProfile(scanner, gymFacade, securityService);
                    case "3" -> deleteTrainee(gymFacade, securityService);
                    case "4" -> viewAvailableTrainers(gymFacade, securityService);
                    case "5" -> viewAssignedTrainers(gymFacade, securityService);
                    case "6" -> addTrainer(scanner, gymFacade, securityService);
                    case "7" -> removeTrainer(scanner, gymFacade, securityService);
                    case "8" -> viewTrainings(scanner, gymFacade, securityService);
                    case "9" -> addTraining(scanner, gymFacade, securityService);
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
            Optional<TraineeDTO> trainee = gymFacade.trainee().findByUsername(username);
            log.info("Trainee Profile: {}", trainee);
        } catch (Exception e) {
            log.error("Error viewing profile: {}", e.getMessage());
        }
    }

    private static void updateProfile(Scanner scanner, GymFacade gymFacade, SecurityService securityService) {
        String username = securityService.getAuthenticatedUsername();

        log.info("\nNEW FIRST NAME (leave blank to keep unchanged): ");
        String firstName = scanner.nextLine();

        log.info("\nNEW LAST NAME (leave blank to keep unchanged): ");
        String lastName = scanner.nextLine();

        log.info("\nNEW ADDRESS (leave blank to keep unchanged): ");
        String address = scanner.nextLine();

        ZonedDateTime dateOfBirth = null;
        log.info("\nNEW DATE OF BIRTH (format: yyyy-mm-dd, leave blank to keep unchanged): ");
        String dob = scanner.nextLine();
        if (!dob.isBlank()) {
            try {
                dateOfBirth = ZonedDateTime.parse(dob + TIME_ZONED);
            } catch (DateTimeParseException e) {
                log.error("Invalid Date of Birth format. Aborting update.");
                return;
            }
        }

        TraineeDTO updatedDTO = new TraineeDTO(null, firstName, lastName, username,
                dateOfBirth, address, true);
        try {
            gymFacade.trainee().update(updatedDTO);
        } catch (Exception e) {
            log.error("Error updating trainee profile: {}", e.getMessage());
        }
    }

    private static void addTrainer(Scanner scanner, GymFacade gymFacade, SecurityService securityService) {
        String traineeUsername = securityService.getAuthenticatedUsername();

        log.info("Enter Trainer Username to add: ");
        String trainerUsername = scanner.nextLine();
        try {
            gymFacade.trainee().addTrainer(traineeUsername, trainerUsername);
        } catch (Exception e) {
            log.error("Error adding trainer: {}", e.getMessage());
        }
    }

    private static void removeTrainer(Scanner scanner, GymFacade gymFacade, SecurityService securityService) {
        String traineeUsername = securityService.getAuthenticatedUsername();

        log.info("Enter Trainer Username to remove: ");
        String trainerUsername = scanner.nextLine();
        try {
            gymFacade.trainee().removeTrainer(traineeUsername, trainerUsername);
        } catch (Exception e) {
            log.error("Error removing trainer: {}", e.getMessage());
        }
    }

    private static void viewAvailableTrainers(GymFacade gymFacade, SecurityService securityService) {
        String traineeUsername = securityService.getAuthenticatedUsername();
        try {
            List<TrainerDTO> availableTrainers = gymFacade.trainee().findAvailableTrainers(traineeUsername);
            if (availableTrainers.isEmpty()) {
                log.info("No available trainers.");
            } else {
                availableTrainers.forEach(trainer -> log.info("{} - {}", trainer.username(), trainer.trainingType()));
            }
        } catch (Exception e) {
            log.error("Error fetching available trainers: {}", e.getMessage());
        }
    }

    private static void viewAssignedTrainers(GymFacade gymFacade, SecurityService securityService) {
        String traineeUsername = securityService.getAuthenticatedUsername();
        try {
            List<TrainerDTO> assignedTrainers = gymFacade.trainee().findAssignedTrainers(traineeUsername);
            if (assignedTrainers.isEmpty()) {
                log.info("No trainers assigned.");
            } else {
                assignedTrainers.forEach(trainer -> log.info("{} - {}", trainer.username(), trainer.trainingType()));
            }
        } catch (Exception e) {
            log.error("Error fetching assigned trainers: {}", e.getMessage());
        }
    }

    private static void viewTrainings(Scanner scanner, GymFacade gymFacade, SecurityService securityService) {
        String traineeUsername = securityService.getAuthenticatedUsername();

        log.info("Enter optional Trainer First Name or Last Name (or leave blank): ");
        String trainerName = scanner.nextLine().trim();

        log.info("Enter optional Training Type (or leave blank): ");
        String trainingType = scanner.nextLine().trim();

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

        List<TrainingDTO> trainings = gymFacade.trainee().findTrainings(traineeUsername, trainerName,
                trainingType, fromDate, toDate);
        if (trainings.isEmpty()) {
            log.info("No trainings found.");
        } else {
            trainings.forEach(training -> log.info(training.toString()));
        }
    }

    private static void addTraining(Scanner scanner, GymFacade gymFacade, SecurityService securityService) {
        String traineeUsername = securityService.getAuthenticatedUsername();

        log.info("Enter Trainer Username: ");
        String trainerUsername = scanner.nextLine();

        log.info("Enter Training Name: ");
        String trainingName = scanner.nextLine();

        ZonedDateTime trainingDate = null;
        log.info("Enter Training Date (format: yyyy-mm-dd): ");
        String date = scanner.nextLine();
        if (!date.isEmpty()) {
            try {
                trainingDate = ZonedDateTime.parse(date + TIME_ZONED);
            } catch (DateTimeParseException e) {
                log.error("Invalid Training Date format. Please enter a valid date in yyyy-mm-dd format.");
                return;
            }
        }

        int trainingDuration = TRAINING_DURATION_INITIALIZATION;
        log.info("Enter Training Duration (in minutes): ");
        String duration = scanner.nextLine();
        try {
            trainingDuration = Integer.parseInt(duration);
        } catch (NumberFormatException e) {
            log.error("Invalid Training Duration. Please enter a valid numeric value.");
        }

        TrainingDTO trainingDTO = new TrainingDTO(null, trainingName, null, trainingDate,
                trainingDuration, traineeUsername, trainerUsername);

        try {
            Optional<Training> createdTraining = gymFacade.trainee().create(trainingDTO);
            createdTraining.ifPresent(training -> log.info("Training added successfully with ID: {}", training.getId()));
        } catch (Exception e) {
            log.error("Error adding training: {}", e.getMessage());
        }
    }

    private static void deleteTrainee(GymFacade gymFacade, SecurityService securityService) {
        String traineeUsername = securityService.getAuthenticatedUsername();
        try {
            gymFacade.trainee().deleteByUsername(traineeUsername);
        } catch (Exception e) {
            log.error("Error deleting profile: {}", e.getMessage());
        }
    }
}