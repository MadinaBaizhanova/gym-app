package com.epam.wca.gym.facade.impl;

import com.epam.wca.gym.dto.UserDTO;
import com.epam.wca.gym.facade.GymFacade;
import com.epam.wca.gym.facade.TraineeFacade;
import com.epam.wca.gym.facade.TrainerFacade;
import com.epam.wca.gym.facade.TrainingFacade;
import com.epam.wca.gym.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GymFacadeImpl implements GymFacade {
    private final TraineeFacade traineeFacade;
    private final TrainerFacade trainerFacade;
    private final TrainingFacade trainingFacade;
    private final UserService userService;

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