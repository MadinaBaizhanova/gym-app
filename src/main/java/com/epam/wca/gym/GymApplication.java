package com.epam.wca.gym;

import com.epam.wca.gym.config.AppConfig;
import com.epam.wca.gym.service.SecurityService;
import com.epam.wca.gym.facade.GymFacade;
import com.epam.wca.gym.utils.cli.TrainerManager;
import com.epam.wca.gym.utils.cli.TraineeManager;
import com.epam.wca.gym.utils.cli.UserManager;
import com.epam.wca.gym.utils.cli.MenuUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

import java.util.Scanner;

/**
 * @deprecated <p>
 * This class previously served as a simple command line interface for interacting with the Gym CRM application.
 * </p>
 * Due to the new, RESTful, version of the application, this class is no longer needed.
 */

@Slf4j
@Deprecated(since = "1.2")
public class GymApplication {

    public static void main(String[] args) {
        AbstractApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        GymFacade gymFacade = context.getBean(GymFacade.class);
        SecurityService securityService = context.getBean(SecurityService.class);
        Scanner scanner = new Scanner(System.in);

        start(scanner, gymFacade, securityService);

        context.close();
        scanner.close();
    }

    private static void start(Scanner scanner, GymFacade gymFacade, SecurityService securityService) {
        log.info("Welcome to the Gym Application!");
        boolean exit = false;

        while (!exit) {
            MenuUtils.displayMenu(
                    "\nMain Menu:",
                    "1. Register",
                    "2. Log in",
                    "3. General User Management",
                    "4. Trainee Management",
                    "5. Trainer Management",
                    "6. Log out",
                    "7. Exit");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> UserManager.register(scanner, gymFacade);
                case "2" -> UserManager.login(scanner, gymFacade, securityService);
                case "3" -> UserManager.showUserManagement(scanner, gymFacade, securityService);
                case "4" -> TraineeManager.showTraineeManagement(scanner, gymFacade, securityService);
                case "5" -> TrainerManager.showTrainerManagement(scanner, gymFacade, securityService);
                case "6" -> UserManager.logout(securityService);
                case "7" -> {
                    log.info("Exiting the application. Goodbye!");
                    exit = true;
                }
                default -> MenuUtils.invalidChoice();
            }
        }
    }
}