package com.pegasus.goplaneje.services;

import com.pegasus.goplaneje.Mappers.UserMapper;
import com.pegasus.goplaneje.dto.request.UserRequestDTO;
import com.pegasus.goplaneje.dto.request.UserUpdateRequestDTO;
import com.pegasus.goplaneje.dto.response.UserResponseDTO;
import com.pegasus.goplaneje.dto.response.UserUpdateResponseDTO;
import com.pegasus.goplaneje.exceptions.CredentialsInUseException;
import com.pegasus.goplaneje.models.User;
import com.pegasus.goplaneje.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;


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
                .id(savedUser.getId().toString())
                .email(savedUser.getEmail())
                .createdAt(savedUser.getCreatedAt())
                .build();
    }

    public Map<String, Object> usersListCreate(List<UserRequestDTO> userListDto) {

        List<String> emails = userListDto.stream()
                .map(UserRequestDTO::getEmail)
                .collect(Collectors.toList());

        Set<String> existingEmails = new HashSet<>(userRepository.findEmailsInList(emails));



        //filtra novos usuarios
        List<User> listNewUsers = userListDto.stream()
                // Remove os usuários que já existem
                .filter(user -> !existingEmails.contains(user.getEmail()))

                .map(dto -> User.builder()
                        .email(dto.getEmail())
                        .password(passwordEncoder.encode(dto.getPassword()))
                        .build())
                .collect(Collectors.toList());


        List<Map<String, String>> falhasDetalhes = userListDto.stream()
                .filter(dto -> existingEmails.contains(dto.getEmail()))
                .map(dto -> {
                    Map<String, String> erro = new HashMap<>();
                    erro.put("email", dto.getEmail());
                    erro.put("erro", "Email já existe");
                    return erro;
                })
                .collect(Collectors.toList());

        //cria os novos usuários
        List<User> usersListSaved = userRepository.saveAll(listNewUsers);

        // Monta o retorno
        Map<String, Object> response = new HashMap<>();
        response.put("total", userListDto.size());
        response.put("criados", usersListSaved.size());
        response.put("falhas", falhasDetalhes.size());
        response.put("falhasDetalhes", falhasDetalhes);

        return response;
    }


    public UserUpdateResponseDTO userUpdate(UUID id, UserUpdateRequestDTO dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        try {


            userMapper.updateUserFromDto(dto, user);

            User updatedUser = userRepository.save(user);


            return UserUpdateResponseDTO.builder()
                    .id(updatedUser.getId().toString())
                    .username(updatedUser.getUsername())
                    .email(updatedUser.getEmail())
                    .role(updatedUser.getRole())
                    .build();
        } catch (Exception e) {
                throw new RuntimeException(e);
        }
    }


    public List<UserResponseDTO> getAllUsers() {
        List<User> usersList = userRepository.findAll();

        if (usersList.isEmpty()) {
            throw new NoSuchElementException("No users found");

        }

        return usersList
                .stream()
                .map(user -> UserResponseDTO.builder()
                        .id(user.getId().toString())
                        .username(user.getUsername())
                        .email(user.getEmail())
                        .createdAt(user.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }

    public UserResponseDTO getUserById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        return UserResponseDTO.builder()
                .id(user.getId().toString())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .build();
    }

    public UserResponseDTO getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        return UserResponseDTO.builder()
                .id(user.getId().toString())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .build();
    }
}