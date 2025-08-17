package com.pegasus.goplaneje.services;

import com.pegasus.goplaneje.dto.request.LoginRequestDTO;
import com.pegasus.goplaneje.dto.request.UserRequestDTO;
import com.pegasus.goplaneje.enums.Role;
import com.pegasus.goplaneje.models.User;
import com.pegasus.goplaneje.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomUserDetailsServiceTest {

    private User user;
    private LoginRequestDTO loginRequestDTO;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        loginRequestDTO = LoginRequestDTO.builder()
                .email("samuel@example.com")
                .password("plaintext123")
                .build();

        user = User.builder()
                .id(UUID.randomUUID())
                .username(null)
                .role(Role.COMMON)
                .createdAt(LocalDateTime.now())
                .email(loginRequestDTO.getEmail())
                .password(loginRequestDTO.getPassword())
                .build();

    }

    @DisplayName("should load user by email(username)")
    @Test
    void shouldLoadUserByUsername() {

        // Arrange
        when(userRepository.findByEmail(loginRequestDTO.getEmail())).thenReturn(Optional.of(user));

        // Act
        UserDetails details = customUserDetailsService.loadUserByUsername(loginRequestDTO.getEmail());

        // Assert
        assertEquals(user, details);
        verify(userRepository, times(1)).findByEmail(loginRequestDTO.getEmail());
    }

    @DisplayName("should throw exception when user not found by email")
    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        // Arrange
        when(userRepository.findByEmail(loginRequestDTO.getEmail())).thenReturn(Optional.empty());

        // Act & Assert
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class,
                () -> customUserDetailsService.loadUserByUsername(loginRequestDTO.getEmail()));

        assertEquals("User not found with email: " + loginRequestDTO.getEmail(), exception.getMessage());
        verify(userRepository, times(1)).findByEmail(loginRequestDTO.getEmail());
    }
}