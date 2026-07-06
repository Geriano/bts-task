package com.bts.task.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
    @NotBlank(message = "field name required") String name,
    @NotBlank(message = "field username required") String username,
    @NotBlank(message = "field password required") @Size(min = 8, message = "minimum password length is 8") String password,
    @NotBlank(message = "field password confirmation required") @Size(min = 8, message = "minimum password length is 8") String passwordConfirmation) {
}
