package com.epam.wca.gym.service;
import com.epam.wca.gym.dto.TrainerDTO;
import com.epam.wca.gym.dto.TrainingDTO;
import com.epam.wca.gym.entity.Trainer;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

public interface TrainerService extends BaseService<Trainer, TrainerDTO> {

    Optional<TrainerDTO> findByUsername(String trainerUsername);

    void update(TrainerDTO dto);

    List<TrainingDTO> findTrainings(
            String trainerUsername,
            String traineeName,
            ZonedDateTime fromDate,
            ZonedDateTime toDate);
}