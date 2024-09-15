package com.epam.wca.gym.facade;

public interface GymFacade {

    TraineeFacade trainee();

    TrainerFacade trainer();

    UserFacade user();
}