package com.epam.wca.gym.controller.impl;

import com.epam.wca.gym.annotation.MonitorEndpoint;
import com.epam.wca.gym.controller.TrainingTypeController;
import com.epam.wca.gym.dto.type.TrainingTypeDTO;
import com.epam.wca.gym.service.TrainingTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/types")
@RequiredArgsConstructor
public class TrainingTypeControllerImpl implements TrainingTypeController {

    private final TrainingTypeService trainingTypeService;

    @MonitorEndpoint("training.type.controller.get")
    @GetMapping
    @Override
    public List<TrainingTypeDTO> getTrainingTypes() {
        return trainingTypeService.getAllTrainingTypes();
    }
}