package com.example.toDo.security.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Key;

@Component
@Slf4j
public class JwtProvider {
    @Value("${demo.app.jwt.secret.path}")
    private String jwtSecretKeyPath;

    @Value("${demo.app.jwt.expiration}")
    private int expiredTime;

    @SneakyThrows
    private Key key() {
        byte[] bytes = Files.readAllBytes(Paths.get(jwtSecretKeyPath));
        return Keys.hmacShaKeyFor(bytes);
    }

    public String getUserNameFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key()).build() //build a JwtParser object to parse and verify token
                .parseClaimsJws(token) //the JwtParser object get a token to parse
                .getBody().get("USER_NAME").toString(); //get username String from payload
    }

    public Boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parse(token); //build a JwtParser object to verify token
            return true;
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }


}
