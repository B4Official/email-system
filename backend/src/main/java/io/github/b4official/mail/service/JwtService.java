package io.github.b4official.mail.service;

import io.github.b4official.mail.domain.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.LocalDate;
import java.util.Date;

@Service
public class JwtService {

    private SecretKey getSigningKey() {
        String secretKey = "4Z5w8x/y+7aH9bV2cE4dG6fI1jK3lM5nO7pQ9rS0uWw=";
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }


    public String generateToken(String username){

        long expiration = 86400000;
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey())
                .compact();
    }

    public String extractUsername(String jwt){
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(jwt)
                .getPayload()
                .getSubject();
    }

    public Boolean tokenIsValid(String jwt){

        try {
            Date expTime = Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(jwt)
                    .getPayload()
                    .getExpiration();
            return expTime.after(new Date());
        } catch (Exception e){
            return false;
        }
    }
}