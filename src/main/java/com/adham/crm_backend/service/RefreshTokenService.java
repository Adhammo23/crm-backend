package com.adham.crm_backend.service;
import com.adham.crm_backend.dto.RefreshTokenResult;
import com.adham.crm_backend.entity.RefreshToken;
import com.adham.crm_backend.entity.User;
import com.adham.crm_backend.repository.RefreshTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.HexFormat;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class RefreshTokenService {


    @Value("${jwt.refresh-token-ttl-days}")
    private long refreshTokenTtlDays;

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenResult createToken(User user){

        LocalDateTime now = LocalDateTime.now();

        String rawToken = generateRefreshToken();
        String hashedToken = hashToken(rawToken);

        RefreshToken refreshToken = RefreshToken.builder()
                .tokenHash(hashedToken)
                .createdAt(now)
                .expiryDate(now.plusDays(refreshTokenTtlDays))
                .revoked(false)
                .user(user)
                .build();

        RefreshToken savedToken = refreshTokenRepository.save(refreshToken);

        return new RefreshTokenResult(rawToken,savedToken);
    }

    public RefreshToken validate(String rawToken){

        String hashedToken = hashToken(rawToken);



            RefreshToken refreshToken  = refreshTokenRepository.findByTokenHash(hashedToken)
                    .orElseThrow(() -> new InvalidRefreshTokenException("Invalid refresh token"));

            if (refreshToken.isRevoked()){
                revokeAllForUser(refreshToken.getUser());
                throw new InvalidRefreshTokenException("Refresh token has been revoked");
            }

            if (refreshToken.isExpired()){
                throw new InvalidRefreshTokenException("Refresh token has expired");
            }

        return refreshToken;
    }

    public void revoke(RefreshToken token){

        token.setRevoked(true);
        refreshTokenRepository.save(token);
    }

    public void revokeAllForUser(User user){

        List<RefreshToken> refreshTokens = refreshTokenRepository.findAllByUser(user);

        if (refreshTokens.isEmpty()){
            return;
        }

        for (RefreshToken token : refreshTokens){
            token.setRevoked(true);
        }
        refreshTokenRepository.saveAll(refreshTokens);
    }


    private String generateRefreshToken(){
        return UUID.randomUUID().toString();
    }

    private String hashToken(String rowToken){
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(rowToken.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error while hashing refresh token", e);
        }
    }
}
