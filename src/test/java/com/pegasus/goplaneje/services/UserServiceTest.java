package com.pegasus.goplaneje.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.*;

import com.pegasus.goplaneje.dto.request.UserRequestDTO;
import com.pegasus.goplaneje.dto.response.UserResponseDTO;
import com.pegasus.goplaneje.enums.Role;
import com.pegasus.goplaneje.exceptions.CredentialsInUseException;
import com.pegasus.goplaneje.models.User;
import com.pegasus.goplaneje.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.crypto.password.PasswordEncoder;

@DisplayName("tests about UserServices")
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private UserRequestDTO userRequestDTO;
    private User user;

    @InjectMocks
    private UserService userService;


    @BeforeEach
    void setUp() {
        userRequestDTO = UserRequestDTO.builder()
                .email("samuel@example.com")
                .password("plaintext123")
                .build();

        user = User.builder()
                .id(1L)
                .email(userRequestDTO.getEmail())
                .password("hashedPassword")
                .createdAt(LocalDateTime.now())
                .role(Role.fromCode(0))
                .build();
    }


    @DisplayName("should create user successfully")
    @Test
    void shouldCreateUserSuccessfully() {
        // Arrange
        when(userRepository.existsByEmail(userRequestDTO.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(userRequestDTO.getPassword())).thenReturn("hashedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        UserResponseDTO response = userService.userCreate(userRequestDTO);

        // Assert
        assertNotNull(response);
        assertEquals("samuel@example.com", response.getEmail());
        assertEquals(1L, response.getId());
        assertNotNull(response.getCreatedAt());

        verify(userRepository, times(1)).existsByEmail(userRequestDTO.getEmail());
        verify(passwordEncoder, times(1)).encode("plaintext123");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @DisplayName("should throw exception when email already exists")
    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists() {
        // Arrange
        when(userRepository.existsByEmail(userRequestDTO.getEmail())).thenReturn(true);

        // Act & Assert
        CredentialsInUseException exception = assertThrows(CredentialsInUseException.class, () ->
                userService.userCreate(userRequestDTO));

        assertEquals("Email already exists: " + userRequestDTO.getEmail(), exception.getMessage());

        verify(userRepository, times(1)).existsByEmail(userRequestDTO.getEmail());
        verify(userRepository, never()).save(any());
        verify(passwordEncoder, never()).encode(any());
    }

    @DisplayName("should return user list")
    @Test
    void shouldReturnUserList() {
        User user1 = User.builder()
                .id(1L)
                .email("samuel@example.com")
                .username("samuel")
                .password("encodedPassword")
                .role(Role.ADMIN)
                .createdAt(LocalDateTime.now())
                .build();

        User user2 = User.builder()
                .id(2L)
                .email("samuel@example.com")
                .username("samurrocha")
                .password("encodedPassword")
                .role(Role.COMMON)
                .createdAt(LocalDateTime.now())
                .build();

        List<User> users = Arrays.asList(user1, user2);

        // Arrange
        when(userRepository.findAll()).thenReturn(users);

        // Act
        List<UserResponseDTO> response = userService.getAllUsers();

        // Assert
        assertNotNull(response, "A lista não deve ser nula");
        assertFalse(response.isEmpty(), "A lista não deve estar vazia");
        assertEquals(2, response.size());
        UserResponseDTO dto = response.get(0);
        assertEquals(user1.getEmail(), dto.getEmail());
        assertEquals(user1.getId(), dto.getId());
        assertEquals(user1.getUsername(), dto.getUsername());
        assertEquals(user1.getCreatedAt(), dto.getCreatedAt());

        verify(userRepository, times(1)).findAll();
    }

    @Test
    void shouldThrowExceptionWhenUserListEmpty() {
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        // Verifica se a exceção correta é lançada
        NoSuchElementException thrown = assertThrows(NoSuchElementException.class, () -> {
            userService.getAllUsers();
        });

        // Verifica a mensagem da exceção
        assertEquals("No users found", thrown.getMessage());

        // Confirma que findAll foi chamado
        verify(userRepository, times(1)).findAll();
    }

}