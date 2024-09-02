package com.epam.wca.gym.facade;

import com.epam.wca.gym.dto.UserDTO;
import com.epam.wca.gym.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GymFacadeImpl implements GymFacade {
    private final TraineeFacade traineeFacade;
    private final TrainerFacade trainerFacade;
    private final TrainingFacade trainingFacade;
    private final UserService userService;

    @Autowired
    public GymFacadeImpl(TraineeFacade traineeFacade,
                         TrainerFacade trainerFacade,
                         TrainingFacade trainingFacade,
                         UserService userService) {
        this.traineeFacade = traineeFacade;
        this.trainerFacade = trainerFacade;
        this.trainingFacade = trainingFacade;
        this.userService = userService;
    }

    @Override
    public List<UserDTO> findAllUsers() {
        return userService.findAll();
    }

    @Override
    public TraineeFacade trainee() {
        return traineeFacade;
    }

    @Override
    public TrainerFacade trainer() {
        return trainerFacade;
    }

    @Override
    public TrainingFacade training() {
        return trainingFacade;
    }
}