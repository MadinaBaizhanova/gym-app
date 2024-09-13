package com.epam.wca.gym.utils.cli;

import com.epam.wca.gym.dto.TraineeDTO;
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
import static com.epam.wca.gym.utils.Constants.CHOICE_6;
import static com.epam.wca.gym.utils.Constants.ENTER_YOUR_CHOICE;
import static com.epam.wca.gym.utils.Constants.INVALID_CHOICE_PLEASE_TRY_AGAIN;
import static com.epam.wca.gym.utils.Constants.RETURNING_TO_MAIN_MENU;

@Slf4j
@RequiredArgsConstructor
public final class TraineeManager {
    private final GymFacade gymFacade;
    private final Scanner scanner;

    public void manageTrainees() {
        String choice;
        do {
            MenuUtils.displayMenu("\n --- TRAINEE MANAGEMENT MENU --- ",
                    "1. Create Trainee",
                    "2. Update Trainee Address",
                    "3. Delete Trainee",
                    "4. Find Trainee by ID",
                    "5. List All Trainees",
                    "6. Return to Main Menu",
                    ENTER_YOUR_CHOICE);

            choice = scanner.nextLine();
            handleTraineeMenuChoice(choice);
        } while (!choice.equals(CHOICE_6));
    }

    private void handleTraineeMenuChoice(String choice) {
        switch (choice) {
            case CHOICE_1 -> createTrainee();
            case CHOICE_2 -> updateTrainee();
            case CHOICE_3 -> deleteTrainee();
            case CHOICE_4 -> findTraineeById();
            case CHOICE_5 -> listAllTrainees();
            case CHOICE_6 -> log.info(RETURNING_TO_MAIN_MENU);
            default -> log.warn(INVALID_CHOICE_PLEASE_TRY_AGAIN);
        }
    }

    private void createTrainee() {
        log.info("Enter Trainee First Name: ");
        String firstName = scanner.nextLine();
        log.info("Enter Trainee Last Name: ");
        String lastName = scanner.nextLine();
        log.info("Enter Trainee Date of Birth (yyyy-MM-dd): ");
        String dateOfBirth = scanner.nextLine();
        log.info("Enter Trainee Address: ");
        String address = scanner.nextLine();
        gymFacade.trainee().create(firstName, lastName, dateOfBirth, address);
    }

    private void updateTrainee() {
        log.info("Enter trainee ID to update: ");
        String traineeIdToUpdate = scanner.nextLine();
        log.info("Enter new address: ");
        String newAddress = scanner.nextLine();
        gymFacade.trainee().update(traineeIdToUpdate, newAddress);
    }

    private void deleteTrainee() {
        log.info("Enter trainee ID to delete: ");
        String traineeIdToDelete = scanner.nextLine();
        gymFacade.trainee().delete(traineeIdToDelete);
    }

    private void findTraineeById() {
        log.info("Enter trainee ID to find: ");
        String traineeIdToFind = scanner.nextLine();
        Optional<TraineeDTO> trainee = gymFacade.trainee().findById(traineeIdToFind);
        trainee.ifPresent(traineeDTO -> log.info("Trainee Info: {}", traineeDTO));
    }

    private void listAllTrainees() {
        List<TraineeDTO> allTraineeDTOs = gymFacade.trainee().findAll();
        log.info("ALL TRAINEES:");
        allTraineeDTOs.forEach(traineeDTO -> log.info(traineeDTO.toString()));
    }
}