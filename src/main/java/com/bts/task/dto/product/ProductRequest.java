package com.bts.task.dto.product;

import java.math.BigDecimal;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ProductRequest(
    @NotBlank(message = "field title required") String title,
    @NotNull(message = "field price required") @Positive(message = "price must be positive") BigDecimal price,
    @NotBlank(message = "field description required") String description,
    @NotBlank(message = "field category required") String category,
    List<String> images) {
}
