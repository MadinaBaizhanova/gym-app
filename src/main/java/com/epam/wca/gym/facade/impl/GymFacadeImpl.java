package com.epam.wca.gym.facade.impl;

import com.epam.wca.gym.facade.GymFacade;
import com.epam.wca.gym.facade.TraineeFacade;
import com.epam.wca.gym.facade.TrainerFacade;
import com.epam.wca.gym.facade.UserFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GymFacadeImpl implements GymFacade {

    private final TraineeFacade traineeFacade;
    private final TrainerFacade trainerFacade;
    private final UserFacade userFacade;

    @Override
    public TraineeFacade trainee() {
        return traineeFacade;
    }

    @Override
    public TrainerFacade trainer() {
        return trainerFacade;
    }

    @Override
    public UserFacade user() {
        return userFacade;
    }
}