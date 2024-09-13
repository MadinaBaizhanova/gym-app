package com.epam.wca.gym.facade.impl;

import com.epam.wca.gym.dto.TrainerDTO;
import com.epam.wca.gym.facade.TrainerFacade;
import com.epam.wca.gym.service.TrainerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TrainerFacadeImpl implements TrainerFacade {
    private final TrainerService trainerService;

    @Override
    public void create(String firstName, String lastName, String trainingType) {
        TrainerDTO trainerDTO = toTrainerDTO(firstName, lastName, trainingType);
        trainerService.create(trainerDTO);
    }

    @Override
    public void update(String trainerId, String newTrainingType) {
        trainerService.update(trainerId, newTrainingType);
    }

    @Override
    public Optional<TrainerDTO> findById(String trainerId) {
        return trainerService.findById(trainerId);
    }

    @Override
    public List<TrainerDTO> findAll() {
        return trainerService.findAll();
    }

    private static TrainerDTO toTrainerDTO(String firstName, String lastName, String trainingType) {
        TrainerDTO trainerDTO = new TrainerDTO();
        trainerDTO.setFirstName(firstName);
        trainerDTO.setLastName(lastName);
        trainerDTO.setTrainingType(trainingType);
        return trainerDTO;
    }
}