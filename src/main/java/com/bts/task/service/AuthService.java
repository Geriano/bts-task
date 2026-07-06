package com.bts.task.service;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bts.task.dto.auth.LoginRequest;
import com.bts.task.dto.auth.RegisterRequest;
import com.bts.task.dto.auth.TokenResponse;
import com.bts.task.exception.AlreadyExistsException;
import com.bts.task.exception.BadRequestException;
import com.bts.task.model.User;
import com.bts.task.repository.UserRepository;
import com.bts.task.security.JwtService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
  @Autowired
  private final UserRepository userRepository;
  @Autowired
  private final PasswordEncoder passwordEncoder;
  @Autowired
  private final AuthenticationManager authenticationManager;
  @Autowired
  private final JwtService jwtService;

  @Transactional()
  public TokenResponse register(RegisterRequest req) {
    if (!req.password().equals(req.passwordConfirmation())) {
      throw new BadRequestException("Password confirmation does not match");
    }
    if (userRepository.existsByUsername(req.username())) {
      throw new AlreadyExistsException("Username already exists: " + req.username());
    }

    Instant now = Instant.now();
    User user = new User();
    user.setName(req.name());
    user.setUsername(req.username());
    user.setPassword(passwordEncoder.encode(req.password()));
    user.setCreatedAt(now);
    user.setUpdatedAt(now);
    userRepository.save(user);

    return new TokenResponse(jwtService.generateAccessToken(user), jwtService.generateRefreshToken(user));
  }

  @Transactional(readOnly = true)
  public TokenResponse login(LoginRequest req) {
    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(req.username(), req.password()));
    User user = (User) authentication.getPrincipal();
    return new TokenResponse(jwtService.generateAccessToken(user), jwtService.generateRefreshToken(user));
  }
}
