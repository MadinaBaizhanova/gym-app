package com.epam.wca.gym.facade.impl;

import com.epam.wca.gym.dto.TrainerDTO;
import com.epam.wca.gym.dto.TrainingDTO;
import com.epam.wca.gym.entity.Trainer;
import com.epam.wca.gym.facade.TrainerFacade;
import com.epam.wca.gym.service.TrainerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
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
    public List<TrainingDTO> findTrainings(String trainerUsername, String traineeName,
                                           ZonedDateTime fromDate, ZonedDateTime toDate) {
        return trainerService.findTrainings(trainerUsername, traineeName, fromDate, toDate);
    }
}