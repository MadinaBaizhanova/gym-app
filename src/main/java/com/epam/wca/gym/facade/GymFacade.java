package com.epam.wca.gym.facade;

import com.epam.wca.gym.dto.UserDTO;

import java.util.List;

public interface GymFacade {
    List<UserDTO> findAllUsers();

    TraineeFacade trainee();

    TrainerFacade trainer();

    TrainingFacade training();
}