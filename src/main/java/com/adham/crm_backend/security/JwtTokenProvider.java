package com.adham.crm_backend.security;

import com.adham.crm_backend.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.access-token-ttl-minutes}")
    private long expiration;

    public String generateAccessToken(User user){

        Set<String> roles = user.getRoles()
                .stream().map(role -> role.getName().name())
                .collect(Collectors.toSet());

        return Jwts.builder()
                .setSubject(user.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+Duration.ofMinutes(expiration).toMillis()))
                .claim("roles",roles)
                .signWith(getSignKey())
                .compact();

    }

    private Claims extractClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String getSubject(String token){
        return extractClaims(token).getSubject();
    }

    public Set<String> getRolesFromToken(String token) {
        Claims claims = extractClaims(token);
        List<String> roles = claims.get("roles",List.class);
        return new HashSet<>(roles);
    }

    public void validateToken(String token) {
        extractClaims(token);
    }
    private SecretKey getSignKey(){
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}

