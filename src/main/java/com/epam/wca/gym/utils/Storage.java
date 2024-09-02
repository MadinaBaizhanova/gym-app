package com.epam.wca.gym.utils;

import com.epam.wca.gym.entity.Trainee;
import com.epam.wca.gym.entity.Trainer;
import com.epam.wca.gym.entity.Training;
import com.epam.wca.gym.entity.User;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Getter
@Component
public class Storage {
    private final Map<Long, User> users = new HashMap<>();
    private final Map<Long, Trainee> trainees = new HashMap<>();
    private final Map<Long, Trainer> trainers = new HashMap<>();
    private final Map<Long, Training> trainings = new HashMap<>();
}