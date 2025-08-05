package com.pegasus.goplaneje.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pegasus.goplaneje.dto.request.LoginRequestDTO;
import com.pegasus.goplaneje.dto.response.AuthResponseDTO;
import com.pegasus.goplaneje.services.AuthService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    private LoginRequestDTO loginDto;

    @MockBean
    private AuthenticationManager authenticationManager;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        loginDto = LoginRequestDTO.builder()
                .email("user@email.com")
                .password("senhaSegura")
                .build();
    }

    @Test
    @DisplayName("Deve autenticar usuário com sucesso e retornar token")
    void loginUser_ComCredenciaisValidas_DeveRetornarToken() throws Exception {
        // Arrange;
        AuthResponseDTO responseDto = new AuthResponseDTO("token-jwt");

        when(authService.login(any(LoginRequestDTO.class))).thenReturn(responseDto);

        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("token-jwt"));
    }

    @Test
    @DisplayName("Deve retornar 400 se email estiver em branco")
    void loginUser_EmailEmBranco_DeveRetornar400() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").exists());
    }

    @Test
    @DisplayName("Deve retornar 400 se senha for nula")
    void loginUser_SenhaNula_DeveRetornar400() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").exists());
    }

    @Test
    @DisplayName("Deve retornar 401 se serviço lançar exceção de autenticação")
    void loginUser_AutenticacaoInvalida_DeveRetornar401() throws Exception {
        when(authService.login(any())).thenThrow(new RuntimeException("Credenciais inválidas"));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isUnauthorized());
    }
}
