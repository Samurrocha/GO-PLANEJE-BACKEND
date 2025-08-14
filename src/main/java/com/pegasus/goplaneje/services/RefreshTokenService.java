package com.pegasus.goplaneje.services;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pegasus.goplaneje.dto.response.AuthResponseDTO;
import com.pegasus.goplaneje.dto.response.RefreshTokenResponseDTO;
import com.pegasus.goplaneje.exceptions.CredentialsInUseException;
import com.pegasus.goplaneje.models.RefreshToken;
import com.pegasus.goplaneje.models.User;
import com.pegasus.goplaneje.repositories.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.keygen.KeyGenerators;
import java.util.Base64;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCrypt;


@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    private JwtService jwtService;
    private UserService userService;

    public String createRefreshToken(User user) {

        String refreshToken = Base64.getUrlEncoder()
        		.withoutPadding()
        		.encodeToString(KeyGenerators.secureRandom(32).generateKey());
        
        refreshToken = BCrypt.hashpw(refreshToken, BCrypt.gensalt());
        
        RefreshToken newRefreshToken = RefreshToken.builder()
        		.user(user)
        		.token(refreshToken)
        		.expiryDate(Instant.now().plus(10, ChronoUnit.DAYS))//10 dias
        		.build();
        
        refreshTokenRepository.save(newRefreshToken);
        
        return refreshToken;
        
    }
    
    public Optional<RefreshToken> findByToken(String token){
    	
    	Optional<RefreshToken> refreshToken = refreshTokenRepository.findByToken(token);
    	
    	 if (!refreshToken.isPresent()) {
    		 throw new NoSuchElementException("No refreshToken found");
         }
    	 
    	 return refreshToken;
    	
    			
    }
    
    public RefreshTokenResponseDTO refreshAccessToken(String acessToken) {
    	
    	RefreshToken refreshToken = refreshTokenRepository.findByToken(acessToken)
    			.orElseThrow(()->new NoSuchElementException("No RefreshToken found"));
    	
    	verifyExpiration(refreshToken);
    	
    	User user = refreshToken.getUser();
    	
    	if (user == null) {
    		throw new NoSuchElementException("No User found");
    	}
    	
    	String newAcessToken = jwtService.generateToken(user);
    	
    	String newRefreshToken = createRefreshToken(user);
    	
    	refreshTokenRepository.delete(refreshToken);
    	
    	return RefreshTokenResponseDTO.builder()
    			.refreshToken(newRefreshToken)
    			.acessToken(newAcessToken)
    			.build();
    	
    }
        

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(token);
            throw new RuntimeException("Refresh token expirado");
        }
        return token;
    }
}
