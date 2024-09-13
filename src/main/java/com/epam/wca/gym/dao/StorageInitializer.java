package com.epam.wca.gym.dao;

import com.epam.wca.gym.entity.Trainee;
import com.epam.wca.gym.entity.Trainer;
import com.epam.wca.gym.entity.Training;
import com.epam.wca.gym.entity.TrainingType;
import com.epam.wca.gym.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.function.Consumer;

import static com.epam.wca.gym.utils.Constants.COLUMN_1;
import static com.epam.wca.gym.utils.Constants.COLUMN_2;
import static com.epam.wca.gym.utils.Constants.COLUMN_3;
import static com.epam.wca.gym.utils.Constants.COLUMN_4;
import static com.epam.wca.gym.utils.Constants.COLUMN_5;
import static com.epam.wca.gym.utils.Constants.COLUMN_6;
import static com.epam.wca.gym.utils.Constants.COLUMN_7;
import static com.epam.wca.gym.utils.Constants.LINE_SPLIT;

@Slf4j
@Component
public final class StorageInitializer implements BeanPostProcessor {

    @Value("${user.data.file.path}")
    private Resource userFile;

    @Value("${trainee.data.file.path}")
    private Resource traineeFile;

    @Value("${trainer.data.file.path}")
    private Resource trainerFile;

    @Value("${training.data.file.path}")
    private Resource trainingFile;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof Storage storage) {
            log.info("Initializing storages with data...");
            loadData(userFile, line -> processUserLine(line, storage));
            loadData(traineeFile, line -> processTraineeLine(line, storage));
            loadData(trainerFile, line -> processTrainerLine(line, storage));
            loadData(trainingFile, line -> processTrainingLine(line, storage));
            log.info("Storages initialized successfully.");
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    private void loadData(Resource file, Consumer<String> lineProcessor) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            reader.lines().forEach(lineProcessor);
        } catch (IOException e) {
            log.error("Failed to load data from file: {}. Error: {}", file.getFilename(), e.getMessage());
        }
    }

    private void processUserLine(String line, Storage storage) {
        String[] data = line.split(LINE_SPLIT);
        Long userId = Long.parseLong(data[COLUMN_1]);
        String firstName = data[COLUMN_2];
        String lastName = data[COLUMN_3];
        String username = data[COLUMN_4];
        String password = data[COLUMN_5];
        boolean isActive = Boolean.parseBoolean(data[COLUMN_6]);

        User user = new User(userId, firstName, lastName, username, password, isActive);
        storage.getUsers().put(userId, user);
    }

    private void processTraineeLine(String line, Storage storage) {
        String[] data = line.split(LINE_SPLIT);
        Long traineeId = Long.parseLong(data[COLUMN_1]);
        Long userId = Long.parseLong(data[COLUMN_2]);
        LocalDate dateOfBirth = LocalDate.parse(data[COLUMN_3]);
        String address = data[COLUMN_4];

        Trainee trainee = new Trainee(traineeId, userId, dateOfBirth, address);
        storage.getTrainees().put(traineeId, trainee);
    }

    private void processTrainerLine(String line, Storage storage) {
        String[] data = line.split(LINE_SPLIT);
        Long trainerId = Long.parseLong(data[COLUMN_1]);
        Long userId = Long.parseLong(data[COLUMN_2]);
        String trainingType = data[COLUMN_3];

        Trainer trainer = new Trainer(trainerId, userId, TrainingType.valueOf(trainingType));
        storage.getTrainers().put(trainerId, trainer);
    }

    private void processTrainingLine(String line, Storage storage) {
        String[] data = line.split(LINE_SPLIT);
        Long trainingId = Long.parseLong(data[COLUMN_1]);
        Long traineeId = Long.parseLong(data[COLUMN_2]);
        Long trainerId = Long.parseLong(data[COLUMN_3]);
        String trainingName = data[COLUMN_4];
        TrainingType trainingType = TrainingType.valueOf(data[COLUMN_5]);
        LocalDate trainingDate = LocalDate.parse(data[COLUMN_6]);
        int trainingDuration = Integer.parseInt(data[COLUMN_7]);

        Training training = new Training(trainingId, traineeId, trainerId, trainingName, trainingType,
                trainingDate, trainingDuration);
        storage.getTrainings().put(trainingId, training);
    }
}