package com.bts.task.dto.auth;

public record TokenResponse(
    String accessToken,
    String refreshToken) {
}
