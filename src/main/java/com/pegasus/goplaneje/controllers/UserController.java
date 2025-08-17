package com.pegasus.goplaneje.controllers;

import com.pegasus.goplaneje.dto.request.UserRequestDTO;
import com.pegasus.goplaneje.dto.request.UserUpdateRequestDTO;
import com.pegasus.goplaneje.dto.response.UserResponseDTO;
import com.pegasus.goplaneje.dto.response.UserUpdateResponseDTO;
import com.pegasus.goplaneje.services.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/userslist")
    public ResponseEntity<List<UserResponseDTO>> listAllUsers() {

        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PostMapping(
            value = "/register"
    )
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserRequestDTO dto, UriComponentsBuilder uriBuilder) {

        UserResponseDTO user = userService.userCreate(dto);

        URI location = uriBuilder
                .path("/register/" + user.getId())
                .buildAndExpand(user.getId())
                .toUri();

        return ResponseEntity.created(location).body(user);
    }

    @PostMapping(
            value = "/register/userslist"
    )
    public ResponseEntity<Map<String, Object>> createUsersList(@Valid @RequestBody List<@Valid UserRequestDTO> listDto) {

        Map<String, Object> usersListResponse = userService.usersListCreate(listDto);

        // Header Location aponta para endpoint de bulk
        URI location = URI.create("/register/userslist/");

        return ResponseEntity.created(location).body(usersListResponse);
    }

    @PatchMapping(
            value = "/update/{id}"
    )
    public ResponseEntity<UserUpdateResponseDTO> updateUser(@PathVariable UUID id, @Valid @RequestBody UserUpdateRequestDTO dto) {

        UserUpdateResponseDTO userUpdated = userService.userUpdate(id, dto);

        return ResponseEntity.ok().body(userUpdated);
    }

    @GetMapping(
            value = "/{id}"
    )
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable UUID id) {

        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping(
            value = "/email"
    )
    public ResponseEntity<UserResponseDTO> getUserByEmail(@RequestBody String email) {

        return ResponseEntity.ok(userService.getUserByEmail(email));
    }
}
