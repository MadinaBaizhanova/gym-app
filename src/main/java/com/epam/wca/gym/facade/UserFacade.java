package com.epam.wca.gym.facade;

import com.epam.wca.gym.entity.Role;

/**
 * @deprecated
 * <p>
 * This interface previously served as a facade interface for user-related operations.
 * It provided a layer between the service layer and the command line interface represented by GymApplication class.
 * </p>
 * The responsibilities of the class implementing this interface
 * have been moved to {@link com.epam.wca.gym.controller.UserController}
 */

@Deprecated(since = "1.2")
public interface UserFacade {

    Role authenticate(String username, String password);

    void activateUser(String username);

    void deactivateUser(String username);

    void changePassword(String username, String currentPassword, String newPassword);
}