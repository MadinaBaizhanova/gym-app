package com.epam.wca.gym.controller;

import com.epam.wca.gym.dto.FindTrainingDTO;
import com.epam.wca.gym.dto.TrainerDTO;
import com.epam.wca.gym.dto.TrainerUpdateDTO;
import com.epam.wca.gym.dto.TrainingDTO;
import com.epam.wca.gym.entity.Trainer;
import com.epam.wca.gym.service.SecurityService;
import com.epam.wca.gym.service.TrainerService;
import com.epam.wca.gym.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/trainer")
@RequiredArgsConstructor
public class TrainerController {

    private final TrainerService trainerService;
    private final UserService userService;
    private final SecurityService securityService;

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody TrainerDTO trainerDTO) {
        Optional<Trainer> registeredTrainer = trainerService.create(trainerDTO);

        if (registeredTrainer.isPresent()) {
            String username = registeredTrainer.get().getUser().getUsername();
            String rawPassword = userService.getRawPassword();

            userService.clearRawPassword();

            Map<String, String> response = new LinkedHashMap<>();
            response.put("username", username);
            response.put("password", rawPassword);

            return ResponseEntity.ok(response);
        }

        return status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Trainer registration failed."));
    }

    @GetMapping("/profile")
    public ResponseEntity<TrainerDTO> getProfile() {
        String username = securityService.getAuthenticatedUsername();

        return trainerService.findByUsername(username)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PutMapping("/update-profile")
    public ResponseEntity<TrainerDTO> updateProfile(@RequestBody TrainerUpdateDTO dto) {
        String username = securityService.getAuthenticatedUsername();

        TrainerDTO updatedTrainer = trainerService.update(new TrainerDTO(null, dto.firstName(),
                dto.lastName(), username, dto.trainingType(), null, null));

        return ResponseEntity.ok(updatedTrainer);
    }

    @GetMapping("/trainings")
    public ResponseEntity<List<TrainingDTO>> getTrainings(@RequestBody FindTrainingDTO dto) {
        String username = securityService.getAuthenticatedUsername();

        List<TrainingDTO> trainings = trainerService.findTrainings(new FindTrainingDTO(username, dto.name(),
                null, dto.fromDate(), dto.toDate()));

        return ResponseEntity.ok(trainings);
    }
}