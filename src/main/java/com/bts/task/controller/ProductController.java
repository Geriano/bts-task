package com.bts.task.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.bts.task.dto.common.PageResponse;
import com.bts.task.dto.product.ProductRequest;
import com.bts.task.dto.product.ProductResponse;
import com.bts.task.model.User;
import com.bts.task.service.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "Product Controller")
public class ProductController {
  @Autowired
  private final ProductService productService;

  @GetMapping
  @Operation(summary = "List and search products")
  public PageResponse<ProductResponse> list(
      @RequestParam(required = false) String search,
      @RequestParam(required = false) String category,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int limit) {
    return productService.search(search, category, page, limit);
  }

  @GetMapping("/{id}")
  @Operation(summary = "Get product by id")
  public ProductResponse getById(@PathVariable UUID id) {
    return productService.getById(id);
  }

  @PostMapping
  @Operation(summary = "Create product")
  public ProductResponse create(@Valid @RequestBody ProductRequest request, @AuthenticationPrincipal User user) {
    return productService.create(request, user);
  }

  @PutMapping("/{id}")
  @Operation(summary = "Update product")
  public ProductResponse update(@PathVariable UUID id, @Valid @RequestBody ProductRequest request,
      @AuthenticationPrincipal User user) {
    return productService.update(id, request, user);
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Delete product")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable UUID id) {
    productService.delete(id);
  }
}
