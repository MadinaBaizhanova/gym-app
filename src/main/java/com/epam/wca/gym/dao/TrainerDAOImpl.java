package com.epam.wca.gym.dao;

import com.epam.wca.gym.entity.Trainer;
import com.epam.wca.gym.entity.TrainingType;
import com.epam.wca.gym.utils.Storage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class TrainerDAOImpl implements TrainerDAO {

    private final Storage storage;

    @Autowired
    public TrainerDAOImpl(Storage storage) {
        this.storage = storage;
    }

    @Override
    public void save(Trainer trainer) {
        storage.getTrainers().put(trainer.getId(), trainer);
    }

    @Override
    public void update(Long id, TrainingType trainingType) {
        Trainer trainer = storage.getTrainers().get(id);
        if (trainer != null) {
            trainer.setTrainingType(trainingType);
        }
    }

    @Override
    public Optional<Trainer> findById(Long id) {
        return Optional.ofNullable(storage.getTrainers().get(id));
    }

    @Override
    public List<Trainer> findAll() {
        return new ArrayList<>(storage.getTrainers().values());
    }
}