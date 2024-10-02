package com.epam.wca.gym.controller;

import com.epam.wca.gym.dto.trainer.TrainerRegistrationDTO;
import com.epam.wca.gym.dto.training.FindTrainingDTO;
import com.epam.wca.gym.dto.trainer.TrainerDTO;
import com.epam.wca.gym.dto.trainer.TrainerUpdateDTO;
import com.epam.wca.gym.dto.training.TrainingDTO;
import com.epam.wca.gym.service.SecurityService;
import com.epam.wca.gym.service.TrainerService;
import com.epam.wca.gym.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZonedDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/trainer")
@RequiredArgsConstructor
public class TrainerController {

    private final TrainerService trainerService;
    private final UserService userService;
    private final SecurityService securityService;

    @PostMapping("/registration")
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, String> register(@Valid @RequestBody TrainerRegistrationDTO dto) {
        String username = trainerService.create(dto).getUser().getUsername();
        String rawPassword = userService.getRawPassword();

        userService.clearRawPassword();

        Map<String, String> response = new LinkedHashMap<>();
        response.put("username", username);
        response.put("password", rawPassword);

        return response;
    }

    @GetMapping("/profile")
    @ResponseStatus(HttpStatus.OK)
    public TrainerDTO get() {
        String username = securityService.getAuthenticatedUsername();

        return trainerService.findByUsername(username);
    }

    @PutMapping("/profile")
    @ResponseStatus(HttpStatus.OK)
    public TrainerDTO update(@Valid @RequestBody TrainerUpdateDTO dto) {
        String username = securityService.getAuthenticatedUsername();

        return trainerService.update(new TrainerDTO(dto.firstName(),
                dto.lastName(), username, dto.trainingType(), null, null));
    }

    @GetMapping("/trainings")
    @ResponseStatus(HttpStatus.OK)
    public List<TrainingDTO> getTrainings(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "fromDate", required = false) ZonedDateTime fromDate,
            @RequestParam(value = "toDate", required = false) ZonedDateTime toDate) {

        String username = securityService.getAuthenticatedUsername();

        return trainerService.findTrainings(new FindTrainingDTO(username, name, null, fromDate, toDate));
    }
}