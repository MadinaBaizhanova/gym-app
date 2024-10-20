package com.epam.wca.gym.security.authorization;

import com.epam.wca.gym.controller.TokenStore;
import com.epam.wca.gym.exception.AuthorizationFailedException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

    @Value("${jwt.secret.key}")
    private String secretKey;
    private static final int ACCESS_TOKEN_EXPIRATION = 300_000;
    private static final int REFRESH_TOKEN_EXPIRATION = 1_800_000;

    private final TokenStore tokenStore;

    @Override
    public String generateAccessToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public String generateRefreshToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public String refreshAccessToken(String refreshToken) {
        if (!isValid(refreshToken) || tokenStore.isInvalidated(refreshToken)) {
            throw new AuthorizationFailedException("Invalid or invalidated refresh token!");
        }

        String username = extractUsername(refreshToken);
        return generateAccessToken(username);
    }

    @Override
    public boolean isValid(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            throw new AuthorizationFailedException("Token has expired!");
        } catch (SignatureException e) {
            throw new AuthorizationFailedException("Invalid token signature!");
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Base64.getDecoder().decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}