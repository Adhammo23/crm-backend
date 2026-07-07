package com.adham.crm_backend.service;

import com.adham.crm_backend.dto.AuthResponse;
import com.adham.crm_backend.dto.LoginRequest;
import com.adham.crm_backend.dto.RefreshRequest;
import com.adham.crm_backend.dto.RefreshTokenResult;
import com.adham.crm_backend.entity.RefreshToken;
import com.adham.crm_backend.entity.User;
import com.adham.crm_backend.repository.UserRepository;
import com.adham.crm_backend.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class AuthService {
    @Value("${jwt.access-token-ttl-minutes}")
    private Long accessTokenTtlMinutes;

    private final UserRepository userRepository;
    private final RefreshTokenService refreshTokenService;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    public AuthResponse login(LoginRequest request){
        authenticationManager.authenticate(
               new  UsernamePasswordAuthenticationToken(
                       request.email(),
                       request.password()
               )
        );

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(()-> new IllegalStateException("Authenticated user not found"));

        String accessToken = jwtTokenProvider.generateAccessToken(user);

        RefreshTokenResult refreshTokenResult = refreshTokenService.createToken(user);

        return new AuthResponse(
                accessToken,
                refreshTokenResult.rawToken(),
                Duration.ofMinutes(accessTokenTtlMinutes).toSeconds()
        );

    }

    public AuthResponse refresh(RefreshRequest request){


        RefreshToken refreshToken =  refreshTokenService.validate(request.refreshToken());

        User user = refreshToken.getUser();

        refreshTokenService.revoke(refreshToken);

        RefreshTokenResult newRefreshToken = refreshTokenService.createToken(user);

        String accessToken = jwtTokenProvider.generateAccessToken(user);

        return new AuthResponse(
                accessToken,
                newRefreshToken.rawToken(),
                Duration.ofMinutes(accessTokenTtlMinutes).toSeconds()
        );
    }
    public void logout(RefreshRequest request){
       RefreshToken token= refreshTokenService.validate(request.refreshToken());
        refreshTokenService.revoke(token);
    }
}
