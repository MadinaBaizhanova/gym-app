package com.epam.wca.gym.dao;

import com.epam.wca.gym.entity.Trainee;
import com.epam.wca.gym.entity.Trainer;
import com.epam.wca.gym.entity.Training;
import com.epam.wca.gym.entity.User;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Component
public class Storage {
    private final Map<Long, User> users = new ConcurrentHashMap<>();
    private final Map<Long, Trainee> trainees = new ConcurrentHashMap<>();
    private final Map<Long, Trainer> trainers = new ConcurrentHashMap<>();
    private final Map<Long, Training> trainings = new ConcurrentHashMap<>();
}