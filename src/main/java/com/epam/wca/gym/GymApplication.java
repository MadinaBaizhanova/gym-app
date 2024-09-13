package com.epam.wca.gym;

import com.epam.wca.gym.config.AppConfig;
import com.epam.wca.gym.dto.UserDTO;
import com.epam.wca.gym.facade.GymFacade;
import com.epam.wca.gym.utils.cli.MenuUtils;
import com.epam.wca.gym.utils.cli.TraineeManager;
import com.epam.wca.gym.utils.cli.TrainerManager;
import com.epam.wca.gym.utils.cli.TrainingManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;
import java.util.Scanner;

import static com.epam.wca.gym.utils.Constants.CHOICE_1;
import static com.epam.wca.gym.utils.Constants.CHOICE_2;
import static com.epam.wca.gym.utils.Constants.CHOICE_3;
import static com.epam.wca.gym.utils.Constants.CHOICE_4;
import static com.epam.wca.gym.utils.Constants.CHOICE_5;
import static com.epam.wca.gym.utils.Constants.ENTER_YOUR_CHOICE;
import static com.epam.wca.gym.utils.Constants.EXITING;
import static com.epam.wca.gym.utils.Constants.INVALID_CHOICE_PLEASE_TRY_AGAIN;

@Slf4j
public class GymApplication {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        GymFacade gymFacade = context.getBean(GymFacade.class);
        Scanner scanner = new Scanner(System.in);

        TraineeManager traineeManager = new TraineeManager(gymFacade, scanner);
        TrainerManager trainerManager = new TrainerManager(gymFacade, scanner);
        TrainingManager trainingManager = new TrainingManager(gymFacade, scanner);

        initiateGymMenu(gymFacade, traineeManager, trainerManager, trainingManager, scanner);
    }

    private static void initiateGymMenu(GymFacade gymFacade, TraineeManager traineeManager, TrainerManager trainerManager, TrainingManager trainingManager, Scanner scanner) {
        String choice;
        do {
            MenuUtils.displayMenu("\n --- GYM MANAGEMENT SYSTEM - MAIN MENU --- ",
                    "1. Manage Trainees",
                    "2. Manage Trainers",
                    "3. Manage Trainings",
                    "4. List All Users",
                    "5. Exit",
                    ENTER_YOUR_CHOICE);

            choice = scanner.nextLine();
            handleMainMenuChoice(choice, gymFacade, traineeManager, trainerManager, trainingManager);
        } while (!choice.equals(CHOICE_5));
        scanner.close();
    }

    private static void handleMainMenuChoice(String choice, GymFacade gymFacade, TraineeManager traineeManager, TrainerManager trainerManager, TrainingManager trainingManager) {
        switch (choice) {
            case CHOICE_1 -> traineeManager.manageTrainees();
            case CHOICE_2 -> trainerManager.manageTrainers();
            case CHOICE_3 -> trainingManager.manageTrainings();
            case CHOICE_4 -> listAllUsers(gymFacade);
            case CHOICE_5 -> log.info(EXITING);
            default -> log.warn(INVALID_CHOICE_PLEASE_TRY_AGAIN);
        }
    }

    private static void listAllUsers(GymFacade gymFacade) {
        List<UserDTO> allUsers = gymFacade.findAllUsers();

        if (allUsers.isEmpty()) {
            log.warn("No users found.");
        } else {
            log.info("\nALL USERS:");
            allUsers.forEach(user -> log.info(user.toString()));
        }
    }
}