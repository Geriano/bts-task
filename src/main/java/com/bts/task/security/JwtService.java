package com.bts.task.security;

import java.util.Date;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
  private final SecretKey secretKey;
  private final JwtProperties properties;

  public JwtService(JwtProperties properties) {
    this.properties = properties;
    this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(properties.secret()));
  }

  public String generateAccessToken(UserDetails userDetails) {
    String role = userDetails.getAuthorities().stream()
        .findFirst()
        .map(GrantedAuthority::getAuthority)
        .orElse("ADMIN"); // no role right now

    final long millis = System.currentTimeMillis();

    return Jwts.builder()
        .subject(userDetails.getUsername()) // "sub" claim — the username
        .claim("role", role) // custom claim for downstream RBAC
        .issuedAt(new Date(millis)) // "iat" — when this token was created
        .expiration(new Date(millis + properties.accessTokenTtl().toMillis()))
        .signWith(secretKey) // JJWT infers algorithm from key size
        .compact(); // produces the final "aaa.bbb.ccc" string
  }

  public String generateRefreshToken(UserDetails userDetails) {
    final long millis = System.currentTimeMillis();

    return Jwts.builder()
        .subject(userDetails.getUsername())
        .claims(Map.of("token_type", "refresh")) // sentinel: marks this as a refresh-only token
        .issuedAt(new Date(millis))
        .expiration(new Date(millis + properties.refreshTokenTtl().toMillis()))
        .signWith(secretKey)
        .compact();
  }

  public boolean isTokenValid(String token, UserDetails userDetails) {
    try {
      final String username = extractUsername(token);

      return username.equals(userDetails.getUsername())
          && !isTokenExpired(token)
          && isAccessToken(token);

    } catch (JwtException | IllegalArgumentException e) {
      // Expired? malformed? tampered signature? — all treated as invalid.
      return false;
    }
  }

  public boolean isRefreshToken(String token) {
    try {
      Object tokenType = extractAllClaims(token).get("token_type");

      return "refresh".equals(tokenType);
    } catch (JwtException | IllegalArgumentException e) {
      return false;
    }
  }

  private boolean isAccessToken(String token) {
    Object tokenType = extractAllClaims(token).get("token_type");

    return tokenType == null || !"refresh".equals(tokenType);
  }

  public String extractUsername(String token) {
    return extractAllClaims(token).getSubject();
  }

  private Claims extractAllClaims(String token) {
    return Jwts.parser()
        .verifyWith(secretKey)
        .build()
        .parseSignedClaims(token)
        .getPayload();
  }

  private boolean isTokenExpired(String token) {
    return extractAllClaims(token).getExpiration().before(new Date());
  }
}
