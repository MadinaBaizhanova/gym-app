package com.epam.wca.gym.facade;

import com.epam.wca.gym.dto.TraineeDTO;
import com.epam.wca.gym.service.TraineeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class TraineeFacadeImpl implements TraineeFacade {
    private final TraineeService traineeService;

    @Autowired
    public TraineeFacadeImpl(TraineeService traineeService) {
        this.traineeService = traineeService;
    }

    @Override
    public void createTrainee(String firstName, String lastName, String dateOfBirth, String address) {
        TraineeDTO dto = toTraineeDTO(firstName, lastName, dateOfBirth, address);
        traineeService.create(dto);
    }

    @Override
    public void updateTrainee(String traineeId, String newAddress) {
        traineeService.update(traineeId, newAddress);
    }

    @Override
    public void deleteTrainee(String traineeId) {
        traineeService.delete(traineeId);
    }

    @Override
    public Optional<TraineeDTO> findTraineeById(String traineeId) {
        return traineeService.findById(traineeId);
    }

    @Override
    public List<TraineeDTO> findAllTrainees() {
        return traineeService.findAll();
    }

    private static TraineeDTO toTraineeDTO(String firstName, String lastName, String dateOfBirth, String address) {
        TraineeDTO dto = new TraineeDTO();
        dto.setFirstName(firstName);
        dto.setLastName(lastName);
        dto.setDateOfBirth(dateOfBirth);
        dto.setAddress(address);
        return dto;
    }
}
