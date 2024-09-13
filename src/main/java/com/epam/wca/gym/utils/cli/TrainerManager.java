package com.epam.wca.gym.utils.cli;

import com.epam.wca.gym.dto.TrainerDTO;
import com.epam.wca.gym.facade.GymFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import static com.epam.wca.gym.utils.Constants.CHOICE_1;
import static com.epam.wca.gym.utils.Constants.CHOICE_2;
import static com.epam.wca.gym.utils.Constants.CHOICE_3;
import static com.epam.wca.gym.utils.Constants.CHOICE_4;
import static com.epam.wca.gym.utils.Constants.CHOICE_5;
import static com.epam.wca.gym.utils.Constants.ENTER_TRAINING_TYPE;
import static com.epam.wca.gym.utils.Constants.ENTER_YOUR_CHOICE;
import static com.epam.wca.gym.utils.Constants.INVALID_CHOICE_PLEASE_TRY_AGAIN;
import static com.epam.wca.gym.utils.Constants.RETURNING_TO_MAIN_MENU;

@Slf4j
@RequiredArgsConstructor
public final class TrainerManager {
    private final GymFacade gymFacade;
    private final Scanner scanner;

    public void manageTrainers() {
        String choice;
        do {
            MenuUtils.displayMenu("\nTRAINER MANAGEMENT MENU",
                    "1. Create Trainer",
                    "2. Update Trainer Training Type",
                    "3. Find Trainer by ID",
                    "4. List All Trainers",
                    "5. Return to Main Menu",
                    ENTER_YOUR_CHOICE);

            choice = scanner.nextLine();
            handleTrainerMenuChoice(choice);
        } while (!choice.equals(CHOICE_5));
    }

    private void handleTrainerMenuChoice(String choice) {
        switch (choice) {
            case CHOICE_1 -> createTrainer();
            case CHOICE_2 -> updateTrainer();
            case CHOICE_3 -> findTrainerById();
            case CHOICE_4 -> listAllTrainers();
            case CHOICE_5 -> log.info(RETURNING_TO_MAIN_MENU);
            default -> log.warn(INVALID_CHOICE_PLEASE_TRY_AGAIN);
        }
    }

    private void createTrainer() {
        log.info("Enter First Name: ");
        String firstName = scanner.nextLine();
        log.info("Enter Last Name: ");
        String lastName = scanner.nextLine();
        log.info(ENTER_TRAINING_TYPE);
        String trainingTypeInput = scanner.nextLine();
        gymFacade.trainer().create(firstName, lastName, trainingTypeInput);
    }

    private void updateTrainer() {
        log.info("Enter Trainer ID to update: ");
        String trainerIdToUpdate = scanner.nextLine();
        log.info(ENTER_TRAINING_TYPE);
        String newTrainingTypeInput = scanner.nextLine();
        gymFacade.trainer().update(trainerIdToUpdate, newTrainingTypeInput);
    }

    private void findTrainerById() {
        log.info("Enter Trainer ID to find: ");
        String trainerIdToFind = scanner.nextLine();
        Optional<TrainerDTO> optionalTrainerDTO = gymFacade.trainer().findById(trainerIdToFind);
        optionalTrainerDTO.ifPresent(trainerDTO -> log.info("Trainer Info: {}", trainerDTO));
    }

    private void listAllTrainers() {
        List<TrainerDTO> allTrainerDTOs = gymFacade.trainer().findAll();
        log.info("ALL TRAINERS:");
        allTrainerDTOs.forEach(trainerDTO -> log.info(trainerDTO.toString()));
    }
}