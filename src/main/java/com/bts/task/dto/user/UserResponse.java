package com.bts.task.dto.user;

import java.util.UUID;
import java.time.Instant;

public record UserResponse(UUID id, String name, String username, Instant createdAt, Instant updatedAt) {
}
