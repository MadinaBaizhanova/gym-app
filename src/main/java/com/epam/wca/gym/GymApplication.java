package com.epam.wca.gym;

import com.epam.wca.gym.config.AppConfig;
import com.epam.wca.gym.dto.TraineeDTO;
import com.epam.wca.gym.dto.TrainerDTO;
import com.epam.wca.gym.entity.Training;
import com.epam.wca.gym.entity.User;
import com.epam.wca.gym.facade.GymFacade;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import static com.epam.wca.gym.utils.Constants.*;

@Slf4j
public class GymApplication {

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        GymFacade gymFacade = context.getBean(GymFacade.class);
        initiateGymMenu(gymFacade);
    }

    private static void initiateGymMenu(GymFacade gymFacade) {
        Scanner scanner = new Scanner(System.in);
        String choice;
        do {
            for (String s : Arrays.asList("\n --- GYM MANAGEMENT SYSTEM - MAIN MENU --- ",
                    "1. Manage Trainees",
                    "2. Manage Trainers",
                    "3. Manage Trainings",
                    "4. List All Users",
                    "5. Exit",
                    ENTER_YOUR_CHOICE)) {
                log.info(s);
            }
            choice = scanner.nextLine();
            switch (choice) {
                case CHOICE_1 -> manageTrainees(gymFacade, scanner);
                case CHOICE_2 -> manageTrainers(gymFacade, scanner);
                case CHOICE_3 -> manageTrainings(gymFacade, scanner);
                case CHOICE_4 -> listAllUsers(gymFacade);
                case CHOICE_5 -> log.info(EXITING);
                default -> log.warn(INVALID_CHOICE_PLEASE_TRY_AGAIN);
            }
        } while (!choice.equals(CHOICE_5));
        scanner.close();
    }

    private static void manageTrainees(GymFacade gymFacade, Scanner scanner) {
        String choice;
        do {
            for (String s : Arrays.asList("\n --- TRAINEE MANAGEMENT MENU --- ",
                    "1. Create Trainee",
                    "2. Update Trainee Address",
                    "3. Delete Trainee",
                    "4. Find Trainee by ID",
                    "5. List All Trainees",
                    "6. Return to Main Menu",
                    ENTER_YOUR_CHOICE)) {
                log.info(s);
            }
            choice = scanner.nextLine();
            switch (choice) {
                case CHOICE_1:
                    log.info("Enter Trainee First Name: ");
                    String firstName = scanner.nextLine();
                    log.info("Enter Trainee Last Name: ");
                    String lastName = scanner.nextLine();
                    log.info("Enter Trainee Date of Birth (yyyy-MM-dd): ");
                    String dateOfBirth = scanner.nextLine();
                    log.info("Enter Trainee Address: ");
                    String address = scanner.nextLine();
                    gymFacade.createTrainee(firstName, lastName, dateOfBirth, address);
                    break;
                case CHOICE_2:
                    log.info("Enter trainee ID to update: ");
                    String traineeIdToUpdate = scanner.nextLine();
                    log.info("Enter new address: ");
                    String newAddress = scanner.nextLine();
                    gymFacade.updateTrainee(traineeIdToUpdate, newAddress);
                    break;
                case CHOICE_3:
                    log.info("Enter trainee ID to delete: ");
                    String traineeIdToDelete = scanner.nextLine();
                    gymFacade.deleteTrainee(traineeIdToDelete);
                    break;
                case CHOICE_4:
                    log.info("Enter trainee ID to find: ");
                    String traineeIdToFind = scanner.nextLine();
                    Optional<TraineeDTO> optionalTraineeDTO = gymFacade.findTraineeById(traineeIdToFind);
                    if (optionalTraineeDTO.isPresent()) {
                        TraineeDTO traineeDTO = optionalTraineeDTO.get();
                        log.info("Trainee Info: {}", traineeDTO);
                    }
                    break;
                case CHOICE_5:
                    List<TraineeDTO> allTraineeDTOs = gymFacade.findAllTrainees();
                    log.info("ALL TRAINEES:");
                    allTraineeDTOs.forEach(traineeDTO -> log.info(traineeDTO.toString()));
                    break;
                case CHOICE_6:
                    log.info(RETURNING_TO_MAIN_MENU);
                    break;
                default:
                    log.warn(INVALID_CHOICE_PLEASE_TRY_AGAIN);
                    break;
            }
        } while (!choice.equals(CHOICE_6));
    }

    private static void manageTrainers(GymFacade gymFacade, Scanner scanner) {
        String choice;
        do {
            log.info("\nTRAINER MANAGEMENT MENU");
            log.info("1. Create Trainer");
            log.info("2. Update Trainer Training Type");
            log.info("3. Find Trainer by ID");
            log.info("4. List All Trainers");
            log.info("5. Return to Main Menu");
            log.info(ENTER_YOUR_CHOICE);
            choice = scanner.nextLine();
            switch (choice) {
                case CHOICE_1:
                    log.info("Enter First Name: ");
                    String firstName = scanner.nextLine();
                    log.info("Enter Last Name: ");
                    String lastName = scanner.nextLine();
                    log.info(ENTER_TRAINING_TYPE_AVAILABLE_TYPES_FITNESS_YOGA_ZUMBA_STRETCHING_CARDIO_CROSSFIT);
                    String trainingTypeInput = scanner.nextLine();
                    gymFacade.createTrainer(firstName, lastName, trainingTypeInput);
                    break;
                case CHOICE_2:
                    log.info("Enter Trainer ID to update: ");
                    String trainerIdToUpdate = scanner.nextLine();
                    log.info(ENTER_TRAINING_TYPE_AVAILABLE_TYPES_FITNESS_YOGA_ZUMBA_STRETCHING_CARDIO_CROSSFIT);
                    String newTrainingTypeInput = scanner.nextLine();
                    gymFacade.updateTrainer(trainerIdToUpdate, newTrainingTypeInput);
                    break;
                case CHOICE_3:
                    log.info("Enter Trainer ID to find: ");
                    String trainerIdToFind = scanner.nextLine();
                    Optional<TrainerDTO> optionalTrainerDTO = gymFacade.findTrainerById(trainerIdToFind);
                    if (optionalTrainerDTO.isPresent()) {
                        TrainerDTO trainerDTO = optionalTrainerDTO.get();
                        log.info("Trainer Info: {}", trainerDTO);
                    }
                    break;
                case CHOICE_4:
                    List<TrainerDTO> allTrainerDTOs = gymFacade.findAllTrainers();
                    log.info("All Trainers:");
                    allTrainerDTOs.forEach(trainerDTO -> log.info(trainerDTO.toString()));
                    break;
                case CHOICE_5:
                    log.info(RETURNING_TO_MAIN_MENU);
                    break;
                default:
                    log.warn(INVALID_CHOICE_PLEASE_TRY_AGAIN);
                    break;
            }
        } while (!choice.equals(CHOICE_5));
    }

    private static void manageTrainings(GymFacade gymFacade, Scanner scanner) {
        String choice;
        do {
            for (String s : Arrays.asList("\nTRAINING MANAGEMENT MENU",
                    "1. Create Training",
                    "2. Find Training by ID",
                    "3. List All Trainings",
                    "4. Return to Main Menu",
                    ENTER_YOUR_CHOICE)) {
                log.info(s);
            }
            choice = scanner.nextLine();
            switch (choice) {
                case CHOICE_1:
                    log.info("Enter Trainee ID: ");
                    String traineeId = scanner.nextLine();
                    log.info("Enter Trainer ID: ");
                    String trainerId = scanner.nextLine();
                    log.info("Enter Training Name: ");
                    String trainingName = scanner.nextLine();
                    log.info(ENTER_TRAINING_TYPE_AVAILABLE_TYPES_FITNESS_YOGA_ZUMBA_STRETCHING_CARDIO_CROSSFIT);
                    String trainingType = scanner.nextLine();
                    log.info("Enter Training Date (yyyy-MM-dd): ");
                    String trainingDate = scanner.nextLine();
                    log.info("Enter Training Duration (in minutes): ");
                    String trainingDuration = scanner.nextLine();
                    gymFacade.createTraining(traineeId, trainerId, trainingName, trainingType, trainingDate, trainingDuration);
                    break;
                case CHOICE_2:
                    log.info("Enter Training ID to find: ");
                    String trainingId = scanner.nextLine();
                    Optional<Training> optionalTraining = gymFacade.findTrainingById(trainingId);
                    if (optionalTraining.isPresent()) {
                        log.info("Training Info: {}", optionalTraining.get());
                    } else {
                        log.warn("Training not found or invalid ID provided.");
                    }
                    break;
                case CHOICE_3:
                    List<Training> allTrainings = gymFacade.findAllTrainings();
                    log.info("\nALL TRAININGS:");
                    allTrainings.forEach(training -> log.info(training.toString()));
                    break;
                case CHOICE_4:
                    log.info(RETURNING_TO_MAIN_MENU);
                    break;
                default:
                    log.warn(INVALID_CHOICE_PLEASE_TRY_AGAIN);
                    break;
            }
        } while (!choice.equals(CHOICE_4));
    }

    private static void listAllUsers(GymFacade gymFacade) {
        List<User> allUsers = gymFacade.findAllUsers();
        if (allUsers.isEmpty()) {
            log.warn("No users found.");
        } else {
            log.info("\nALL USERS:");
            allUsers.forEach(user -> log.info(user.toString()));
        }
    }
}