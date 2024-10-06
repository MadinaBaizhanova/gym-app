package com.epam.wca.gym.validation;

import com.epam.wca.gym.dao.TrainingTypeDAO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TrainingTypeValidator implements ConstraintValidator<ValidTrainingType, String> {

    private final TrainingTypeDAO trainingTypeDAO;

    @Override
    public boolean isValid(String trainingType, ConstraintValidatorContext context) {

        if (trainingType == null || trainingType.trim().isEmpty()) {
            return true;
        }

        return trainingTypeDAO.findByName(trainingType.toUpperCase()).isPresent();
    }
}