package com.epam.wca.gym.facade;

import com.epam.wca.gym.dto.trainer.TrainerRegistrationDTO;
import com.epam.wca.gym.dto.training.FindTrainingDTO;
import com.epam.wca.gym.dto.trainer.TrainerDTO;
import com.epam.wca.gym.dto.training.TrainingDTO;
import com.epam.wca.gym.entity.Trainer;

import java.util.List;
import java.util.Optional;

/**
 * @deprecated
 * <p>
 * This interface previously served as a facade interface for trainer-related operations.
 * It provided a layer between the service layer and the command line interface represented by GymApplication class.
 * </p>
 * The responsibilities of the class implementing this interface
 * have been moved to {@link com.epam.wca.gym.controller.TrainerController}
 */

@Deprecated(since = "1.2")
public interface TrainerFacade {

    Optional<Trainer> create(TrainerRegistrationDTO trainerDTO);

    Optional<TrainerDTO> findByUsername(String trainerUsername);

    void update(TrainerDTO trainerDTO);

    List<TrainingDTO> findTrainings(FindTrainingDTO dto);
}