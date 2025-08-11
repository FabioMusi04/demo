package me.demo.security;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;

import me.demo.configuration.JwtConfig;

@Service
public class TokenProvider {
  private final JwtConfig jwtConfig;

  public TokenProvider(JwtConfig jwtConfig) {
    this.jwtConfig = jwtConfig;
  }

  public String generateAccessToken(UserDetails user) {
    try {
      Algorithm algorithm = Algorithm.HMAC256(jwtConfig.getSecret());
      return JWT.create()
          .withSubject(user.getUsername())
          .withClaim("username", user.getUsername())
          .withExpiresAt(genAccessExpirationDate())
          .sign(algorithm);
    } catch (JWTCreationException exception) {
      throw new JWTCreationException("Error while generating token", exception);
    }
  }

  public String validateToken(String token) {
    try {
      Algorithm algorithm = Algorithm.HMAC256(jwtConfig.getSecret());
      return JWT.require(algorithm)
          .build()
          .verify(token)
          .getSubject();
    } catch (JWTVerificationException exception) {
      throw new JWTVerificationException("Error while validating token", exception);
    }
  }

  private Instant genAccessExpirationDate() {
    return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
  }
}