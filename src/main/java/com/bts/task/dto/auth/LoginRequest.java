package com.bts.task.dto.auth;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
    @NotBlank(message = "field username required") String username,
    @NotBlank(message = "field password required") String password) {
}
