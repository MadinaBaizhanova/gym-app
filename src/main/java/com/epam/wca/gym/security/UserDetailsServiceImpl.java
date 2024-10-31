package com.epam.wca.gym.security;

import com.epam.wca.gym.dao.TraineeDAO;
import com.epam.wca.gym.dao.TrainerDAO;
import com.epam.wca.gym.dao.UserDAO;
import com.epam.wca.gym.entity.Role;
import com.epam.wca.gym.entity.User;
import com.epam.wca.gym.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserDAO userDAO;
    private final TraineeDAO traineeDAO;
    private final TrainerDAO trainerDAO;

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userDAO.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Role role = determineUserRole(username);

        return new UserDetailsImpl(user, getAuthorities(role));
    }

    private Collection<? extends GrantedAuthority> getAuthorities(Role role) {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    private Role determineUserRole(String username) {
        if (traineeDAO.findByUsername(username).isPresent()) {
            return Role.TRAINEE;
        }

        if (trainerDAO.findByUsername(username).isPresent()) {
            return Role.TRAINER;
        }

        return Role.NONE;
    }
}