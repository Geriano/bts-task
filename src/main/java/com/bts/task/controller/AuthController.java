package com.bts.task.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bts.task.dto.auth.LoginRequest;
import com.bts.task.dto.auth.RegisterRequest;
import com.bts.task.dto.auth.TokenResponse;
import com.bts.task.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Auth Controller")
public class AuthController {
  @Autowired
  private final AuthService authService;

  @PostMapping("/register")
  @Operation(summary = "Register a new user")
  public TokenResponse register(@Valid @RequestBody RegisterRequest request) {
    return authService.register(request);
  }

  @PostMapping("/login")
  @Operation(summary = "Login and obtain tokens")
  public TokenResponse login(@Valid @RequestBody LoginRequest request) {
    return authService.login(request);
  }
}
