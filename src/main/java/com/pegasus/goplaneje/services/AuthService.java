package com.pegasus.goplaneje.services;

import com.pegasus.goplaneje.dto.request.LoginRequestDTO;
import com.pegasus.goplaneje.dto.response.AuthResponseDTO;
import com.pegasus.goplaneje.exceptions.InvalidCredentialsException;
import com.pegasus.goplaneje.models.User;
import com.pegasus.goplaneje.repositories.UserRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Data
@RequiredArgsConstructor
@Service
public class AuthService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final PasswordEncoder passwordEncoder;

    public AuthResponseDTO login(LoginRequestDTO dto) {

        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        boolean isPasswordValid = passwordEncoder.matches(dto.getPassword(), user.getPassword());

        if (!isPasswordValid) {
            throw new InvalidCredentialsException();
        }

        return AuthResponseDTO.builder()
                .acessToken(jwtService.generateToken(user))
                .refreshToken(refreshTokenService.createRefreshToken(user))
                .build();

    }

}
