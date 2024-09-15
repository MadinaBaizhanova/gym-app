package com.epam.wca.gym.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.math.BigInteger;
import java.time.ZonedDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "training")
public class Training {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigInteger id;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "trainee_id", referencedColumnName = "id")
    private Trainee trainee;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "trainer_id", referencedColumnName = "id")
    private Trainer trainer;

    @NonNull
    @Column(name = "training_name", nullable = false)
    private String trainingName;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "training_type_id", referencedColumnName = "id")
    private TrainingType trainingType;

    @NonNull
    @Column(name = "training_date", nullable = false)
    private ZonedDateTime trainingDate;

    @Column(name = "training_duration", nullable = false)
    private int trainingDuration;
}