package com.epam.wca.gym.controller;

import com.epam.wca.gym.dto.type.TrainingTypeDTO;
import com.epam.wca.gym.service.TrainingTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/training-types")
@RequiredArgsConstructor
public class TrainingTypeController {

    private final TrainingTypeService trainingTypeService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<TrainingTypeDTO> getTrainingTypes() {
        return trainingTypeService.getAllTrainingTypes();
    }
}