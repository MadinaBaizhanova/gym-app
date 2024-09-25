package com.epam.wca.gym.facade.impl;

import com.epam.wca.gym.facade.GymFacade;
import com.epam.wca.gym.facade.TraineeFacade;
import com.epam.wca.gym.facade.TrainerFacade;
import com.epam.wca.gym.facade.UserFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @deprecated
 * <p>
 * This class previously served as a facade for combining all three facade classes - Trainee, Trainer, and User.
 * It provided a layer between the service layer and the command line interface represented by GymApplication class.
 * </p>
 * Due to the new, RESTful, version of the application, this class is no longer needed.
 */

@Component
@RequiredArgsConstructor
@Deprecated(since = "1.2")
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