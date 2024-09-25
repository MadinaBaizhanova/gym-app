package com.epam.wca.gym.facade;

import com.epam.wca.gym.dto.FindTrainingDTO;
import com.epam.wca.gym.dto.TraineeDTO;
import com.epam.wca.gym.dto.TrainerInListDTO;
import com.epam.wca.gym.dto.TrainingDTO;
import com.epam.wca.gym.entity.Trainee;
import com.epam.wca.gym.entity.Training;

import java.util.List;
import java.util.Optional;

/**
 * @deprecated
 * <p>
 * This interface previously served as a facade interface for trainee-related operations.
 * It provided a layer between the service layer and the command line interface represented by GymApplication class.
 * </p>
 * The responsibilities of the class implementing this interface
 * have been moved to {@link com.epam.wca.gym.controller.TraineeController}
 */

@Deprecated(since = "1.2")
public interface TraineeFacade {

    Optional<Trainee> create(TraineeDTO traineeDTO);

    Optional<TraineeDTO> findByUsername(String username);

    void deleteByUsername(String username);

    void update(TraineeDTO traineeDTO);

    void addTrainer(String traineeUsername, String trainerUsername);

    void removeTrainer(String traineeUsername, String trainerUsername);

    List<TrainerInListDTO> findAvailableTrainers(String traineeUsername);

    List<TrainerInListDTO> findAssignedTrainers(String traineeUsername);

    List<TrainingDTO> findTrainings(FindTrainingDTO dto);

    Optional<Training> create(TrainingDTO trainingDTO);
}