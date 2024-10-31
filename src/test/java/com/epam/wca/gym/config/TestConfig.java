package com.epam.wca.gym.config;

import com.epam.wca.gym.security.authorization.JwtAuthenticationFilter;
import com.epam.wca.gym.security.authorization.JwtService;
import com.epam.wca.gym.security.authorization.JwtServiceImpl;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
public class TestConfig {

    @Bean
    @Primary
    public JwtService jwtService() {
        return Mockito.mock(JwtServiceImpl.class);
    }

    @Bean
    @Primary
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return Mockito.mock(JwtAuthenticationFilter.class);
    }
}
