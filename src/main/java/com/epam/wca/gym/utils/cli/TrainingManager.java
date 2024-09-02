package com.epam.wca.gym.utils.cli;

import com.epam.wca.gym.dto.TrainingDTO;
import com.epam.wca.gym.facade.GymFacade;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import static com.epam.wca.gym.utils.Constants.*;

@Slf4j
public final class TrainingManager {
    private final GymFacade gymFacade;
    private final Scanner scanner;

    public TrainingManager(GymFacade gymFacade, Scanner scanner) {
        this.gymFacade = gymFacade;
        this.scanner = scanner;
    }

    public void manageTrainings() {
        String choice;
        do {
            MenuUtils.displayMenu("\nTRAINING MANAGEMENT MENU",
                    "1. Create Training",
                    "2. Find Training by ID",
                    "3. List All Trainings",
                    "4. Return to Main Menu",
                    ENTER_YOUR_CHOICE);

            choice = scanner.nextLine();
            handleTrainingMenuChoice(choice);
        } while (!choice.equals(CHOICE_4));
    }

    private void handleTrainingMenuChoice(String choice) {
        switch (choice) {
            case CHOICE_1 -> createTraining();
            case CHOICE_2 -> findTrainingById();
            case CHOICE_3 -> listAllTrainings();
            case CHOICE_4 -> log.info(RETURNING_TO_MAIN_MENU);
            default -> log.warn(INVALID_CHOICE_PLEASE_TRY_AGAIN);
        }
    }

    private void createTraining() {
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
        gymFacade.training().createTraining(traineeId, trainerId, trainingName, trainingType, trainingDate, trainingDuration);
    }

    private void findTrainingById() {
        log.info("Enter Training ID to find: ");
        String trainingId = scanner.nextLine();
        Optional<TrainingDTO> trainingDTO = gymFacade.training().findTrainingById(trainingId);
        trainingDTO.ifPresent(dto -> log.info("Training Info: {}", dto));
    }

    private void listAllTrainings() {
        List<TrainingDTO> allTrainings = gymFacade.training().findAllTrainings();
        log.info("\nALL TRAININGS:");
        allTrainings.forEach(training -> log.info(training.toString()));
    }
}