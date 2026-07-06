package com.bts.task.dto.product;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ProductResponse(
    UUID id,
    String title,
    BigDecimal price,
    String description,
    String category,
    String createdBy,
    Instant createdAt,
    Instant updatedAt,
    List<String> images) {
}
