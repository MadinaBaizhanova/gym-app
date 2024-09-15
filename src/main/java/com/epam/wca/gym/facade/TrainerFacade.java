package com.epam.wca.gym.facade;

import com.epam.wca.gym.dto.TrainerDTO;
import com.epam.wca.gym.dto.TrainingDTO;
import com.epam.wca.gym.entity.Trainer;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

public interface TrainerFacade {

    Optional<Trainer> create(TrainerDTO trainerDTO);

    Optional<TrainerDTO> findByUsername(String trainerUsername);

    void update(TrainerDTO trainerDTO);

    List<TrainingDTO> findTrainings(String trainerUsername, String traineeName,
                                    ZonedDateTime fromDate, ZonedDateTime toDate);
}