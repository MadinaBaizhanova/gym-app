package com.epam.wca.gym.facade;

/**
 * @deprecated
 * <p>
 * This interface previously served as a facade interface for combining all three facade classes -
 * Trainee, Trainer, and User.
 * It provided a layer between the service layer and the command line interface represented by GymApplication class.
 * </p>
 * Due to the new, RESTful, version of the application, this interface is no longer needed.
 */

@Deprecated(since = "1.2")
public interface GymFacade {

    TraineeFacade trainee();

    TrainerFacade trainer();

    UserFacade user();
}