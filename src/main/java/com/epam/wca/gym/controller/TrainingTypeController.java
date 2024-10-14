package com.epam.wca.gym.controller;

import com.epam.wca.gym.annotation.MonitorEndpoint;
import com.epam.wca.gym.dto.type.TrainingTypeDTO;
import com.epam.wca.gym.service.TrainingTypeService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/types")
@RequiredArgsConstructor
public class TrainingTypeController {

    private final TrainingTypeService trainingTypeService;

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TrainingTypeDTO.class)))
    })
    @MonitorEndpoint("training.type.controller.get")
    @GetMapping
    public List<TrainingTypeDTO> getTrainingTypes() {
        return trainingTypeService.getAllTrainingTypes();
    }
}