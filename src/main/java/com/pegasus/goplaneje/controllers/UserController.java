package com.pegasus.goplaneje.controllers;

import com.pegasus.goplaneje.dto.request.UserRequestDTO;
import com.pegasus.goplaneje.dto.response.UserResponseDTO;
import com.pegasus.goplaneje.services.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;

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
                .path("api/users/{id}")
                .buildAndExpand(user.getId())
                .toUri();

        return ResponseEntity.created(location).body(user);
    }


}
