package com.pegasus.goplaneje.services;

import com.pegasus.goplaneje.dto.request.UserRequestDTO;
import com.pegasus.goplaneje.dto.response.UserResponseDTO;
import com.pegasus.goplaneje.exceptions.CredentialsInUseException;
import com.pegasus.goplaneje.models.User;
import com.pegasus.goplaneje.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponseDTO userCreate(UserRequestDTO dto) {
        boolean userExists = userRepository.existsByEmail(dto.getEmail());

        if (userExists) {
            throw new CredentialsInUseException(dto.getEmail());
        }

        User newUser = User.builder()
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .build();

        User savedUser = userRepository.save(newUser);

        return UserResponseDTO.builder()
                .username(savedUser.getUsername())
                .id(savedUser.getId())
                .email(savedUser.getEmail())
                .createdAt(savedUser.getCreatedAt())
                .build();
    }

    public List<UserResponseDTO> getAllUsers() {
        List<User> usersList = userRepository.findAll();

        if (usersList.isEmpty()) {
            throw new NoSuchElementException("No users found");

        }

        return usersList
                .stream()
                .map(user -> UserResponseDTO.builder()
                        .id(user.getId())
                        .username(user.getUsername())
                        .email(user.getEmail())
                        .createdAt(user.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }
}