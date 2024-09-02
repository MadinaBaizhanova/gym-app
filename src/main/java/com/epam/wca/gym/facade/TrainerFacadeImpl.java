package com.epam.wca.gym.facade;

import com.epam.wca.gym.dto.TrainerDTO;
import com.epam.wca.gym.service.TrainerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class TrainerFacadeImpl implements TrainerFacade {
    private final TrainerService trainerService;

    @Autowired
    public TrainerFacadeImpl(TrainerService trainerService) {
        this.trainerService = trainerService;
    }

    @Override
    public void createTrainer(String firstName, String lastName, String trainingTypeStr) {
        TrainerDTO trainerDTO = toTrainerDTO(firstName, lastName, trainingTypeStr);
        trainerService.create(trainerDTO);
    }

    @Override
    public void updateTrainer(String trainerIdStr, String newTrainingTypeStr) {
        trainerService.update(trainerIdStr, newTrainingTypeStr);
    }

    @Override
    public Optional<TrainerDTO> findTrainerById(String trainerIdStr) {
        return trainerService.findById(trainerIdStr);
    }

    @Override
    public List<TrainerDTO> findAllTrainers() {
        return trainerService.findAll();
    }

    private static TrainerDTO toTrainerDTO(String firstName, String lastName, String trainingTypeStr) {
        TrainerDTO trainerDTO = new TrainerDTO();
        trainerDTO.setFirstName(firstName);
        trainerDTO.setLastName(lastName);
        trainerDTO.setTrainingType(trainingTypeStr);
        return trainerDTO;
    }
}