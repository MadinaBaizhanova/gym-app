package com.epam.wca.gym.facade.impl;

import com.epam.wca.gym.dto.trainer.TrainerRegistrationDTO;
import com.epam.wca.gym.dto.trainer.TrainerUpdateDTO;
import com.epam.wca.gym.dto.training.FindTrainingDTO;
import com.epam.wca.gym.dto.trainer.TrainerDTO;
import com.epam.wca.gym.dto.training.TrainingDTO;
import com.epam.wca.gym.entity.Trainer;
import com.epam.wca.gym.facade.TrainerFacade;
import com.epam.wca.gym.service.TrainerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @deprecated <p>
 * This class previously served as a facade for trainer-related operations.
 * It provided a layer between the service layer and the command line interface represented by GymApplication class.
 * </p>
 * The responsibilities of this class have been moved to {@link com.epam.wca.gym.controller.TrainerController}
 */

@Component
@RequiredArgsConstructor
@Deprecated(since = "1.2")
public class TrainerFacadeImpl implements TrainerFacade {

    private final TrainerService trainerService;

    @Override
    public Trainer create(TrainerRegistrationDTO dto) {
        return trainerService.create(dto);
    }

    @Override
    public TrainerDTO findByUsername(String username) {
        return trainerService.findByUsername(username);
    }

    @Override
    public void update(TrainerUpdateDTO dto) {
        trainerService.update(dto);
    }

    @Override
    public List<TrainingDTO> findTrainings(FindTrainingDTO dto) {
        return trainerService.findTrainings(dto);
    }
}