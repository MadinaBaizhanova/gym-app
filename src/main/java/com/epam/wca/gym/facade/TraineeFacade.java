package com.epam.wca.gym.facade;

import com.epam.wca.gym.dto.trainee.TraineeUpdateDTO;
import com.epam.wca.gym.dto.training.FindTrainingDTO;
import com.epam.wca.gym.dto.trainee.TraineeDTO;
import com.epam.wca.gym.dto.trainer.TrainerForTraineeDTO;
import com.epam.wca.gym.dto.training.TrainingDTO;
import com.epam.wca.gym.dto.trainee.TraineeRegistrationDTO;
import com.epam.wca.gym.entity.Trainee;
import com.epam.wca.gym.entity.Training;

import java.util.List;

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

    Trainee create(TraineeRegistrationDTO traineeDTO);

    TraineeDTO findByUsername(String username);

    void deleteByUsername(String username);

    void update(TraineeUpdateDTO traineeDTO);

    void addTrainer(String traineeUsername, String trainerUsername);

    void removeTrainer(String traineeUsername, String trainerUsername);

    List<TrainerForTraineeDTO> findAvailableTrainers(String traineeUsername);

    List<TrainerForTraineeDTO> findAssignedTrainers(String traineeUsername);

    List<TrainingDTO> findTrainings(FindTrainingDTO dto);

    Training create(TrainingDTO trainingDTO);
}