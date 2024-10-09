package com.epam.wca.gym.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.math.BigInteger;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "trainee")
public class Trainee {

    private static final int ALLOCATION_SIZE = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "trainee_seq")
    @SequenceGenerator(name = "trainee_seq", sequenceName = "trainee_sequence", allocationSize = ALLOCATION_SIZE)
    private BigInteger id;

    @Column(name = "date_of_birth")
    private ZonedDateTime dateOfBirth;

    @Column(name = "address")
    private String address;

    @NonNull
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "trainee_trainer",
            joinColumns = @JoinColumn(name = "trainee_id"),
            inverseJoinColumns = @JoinColumn(name = "trainer_id")
    )
    @EqualsAndHashCode.Exclude
    private List<Trainer> trainers = new ArrayList<>();

    @OneToMany(mappedBy = "trainee", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @EqualsAndHashCode.Exclude
    private List<Training> trainings = new ArrayList<>();
}