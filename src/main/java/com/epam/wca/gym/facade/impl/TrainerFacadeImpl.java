package com.epam.wca.gym.facade.impl;

import com.epam.wca.gym.dto.FindTrainingDTO;
import com.epam.wca.gym.dto.TrainerDTO;
import com.epam.wca.gym.dto.TrainingDTO;
import com.epam.wca.gym.entity.Trainer;
import com.epam.wca.gym.facade.TrainerFacade;
import com.epam.wca.gym.service.TrainerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * @deprecated
 * <p>
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
    public Optional<Trainer> create(TrainerDTO trainerDTO) {
        return trainerService.create(trainerDTO);
    }

    @Override
    public Optional<TrainerDTO> findByUsername(String trainerUsername) {
        return trainerService.findByUsername(trainerUsername);
    }

    @Override
    public void update(TrainerDTO trainerDTO) {
        trainerService.update(trainerDTO);
    }

    @Override
    public List<TrainingDTO> findTrainings(FindTrainingDTO dto) {
        return trainerService.findTrainings(dto);
    }
}