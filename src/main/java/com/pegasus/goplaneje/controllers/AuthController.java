package com.pegasus.goplaneje.controllers;

import com.pegasus.goplaneje.dto.request.LoginRequestDTO;
import com.pegasus.goplaneje.dto.response.AuthResponseDTO;
import com.pegasus.goplaneje.dto.response.RefreshTokenResponseDTO;
import com.pegasus.goplaneje.services.AuthService;
import com.pegasus.goplaneje.services.RefreshTokenService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;
import javax.validation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> loginUser(@RequestBody @Valid LoginRequestDTO dto) {

        return ResponseEntity.ok().body(authService.login(dto));
    }
   
        @PostMapping("/refreshtoken")
        public ResponseEntity<RefreshTokenResponseDTO> refreshToken(@RequestBody @Valid String refreshToken) {

            return ResponseEntity.ok().body(refreshTokenService.refreshAccessToken(refreshToken));
        }

    }



