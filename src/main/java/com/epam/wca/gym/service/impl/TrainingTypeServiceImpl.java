package com.epam.wca.gym.service.impl;

import com.epam.wca.gym.dao.TrainingTypeDAO;
import com.epam.wca.gym.dto.TrainingTypeDTO;
import com.epam.wca.gym.entity.TrainingType;
import com.epam.wca.gym.service.TrainingTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TrainingTypeServiceImpl implements TrainingTypeService {

    private final TrainingTypeDAO trainingTypeDAO;

    @Override
    public List<TrainingTypeDTO> getAllTrainingTypes() {
        List<TrainingType> trainingTypes = trainingTypeDAO.findAll();
        return trainingTypes.stream()
                .map(type -> new TrainingTypeDTO(type.getId(), type.getTrainingTypeName()))
                .toList();
    }
}