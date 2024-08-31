package com.epam.wca.gym.service;

import com.epam.wca.gym.dto.TraineeDTO;

import java.util.List;
import java.util.Optional;

public interface TraineeService {

    boolean create(String firstName, String lastName, String dateOfBirthStr, String address);

    boolean update(String traineeIdStr, String newAddress);

    boolean delete(String traineeIdStr);

    Optional<TraineeDTO> findById(String traineeIdStr);

    List<TraineeDTO> findAll();
}