package com.epam.wca.gym.facade.impl;

import com.epam.wca.gym.dto.TraineeDTO;
import com.epam.wca.gym.facade.TraineeFacade;
import com.epam.wca.gym.service.TraineeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TraineeFacadeImpl implements TraineeFacade {
    private final TraineeService traineeService;

    @Override
    public void create(String firstName, String lastName, String dateOfBirth, String address) {
        TraineeDTO dto = toTraineeDTO(firstName, lastName, dateOfBirth, address);
        traineeService.create(dto);
    }

    @Override
    public void update(String traineeId, String newAddress) {
        traineeService.update(traineeId, newAddress);
    }

    @Override
    public void delete(String traineeId) {
        traineeService.delete(traineeId);
    }

    @Override
    public Optional<TraineeDTO> findById(String traineeId) {
        return traineeService.findById(traineeId);
    }

    @Override
    public List<TraineeDTO> findAll() {
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
