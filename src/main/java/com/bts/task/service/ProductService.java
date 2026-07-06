package com.bts.task.service;

import java.time.Instant;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bts.task.dto.common.PageResponse;
import com.bts.task.dto.product.ProductRequest;
import com.bts.task.dto.product.ProductResponse;
import com.bts.task.exception.NotFoundException;
import com.bts.task.model.Product;
import com.bts.task.model.User;
import com.bts.task.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {
  @Autowired
  private final ProductRepository productRepository;

  public PageResponse<ProductResponse> search(String keyword, String category, int page, int limit) {
    Pageable pageable = PageRequest.of(page, limit);
    Page<Product> result = productRepository.search(keyword == null ? "" : keyword, category, pageable);

    return PageResponse.from(result.map(this::toResponse));
  }

  public ProductResponse getById(UUID id) {
    return toResponse(findOrThrow(id));
  }

  @Transactional()
  public ProductResponse create(ProductRequest req, User user) {
    Instant now = Instant.now();
    Product product = new Product();

    product.setTitle(req.title());
    product.setPrice(req.price());
    product.setDescription(req.description());
    product.setCategory(req.category());
    product.setCreatedBy(user.getUsername());
    product.setCreatedById(user.getId());
    product.setUpdatedBy(user.getUsername());
    product.setUpdatedById(user.getId());
    product.setCreatedAt(now);
    product.setUpdatedAt(now);

    return toResponse(productRepository.save(product));
  }

  @Transactional()
  public ProductResponse update(UUID id, ProductRequest req, User user) {
    Product product = findOrThrow(id);

    product.setTitle(req.title());
    product.setPrice(req.price());
    product.setDescription(req.description());
    product.setCategory(req.category());
    product.setUpdatedBy(user.getUsername());
    product.setUpdatedById(user.getId());
    product.setUpdatedAt(Instant.now());

    return toResponse(productRepository.save(product));
  }

  @Transactional()
  public void delete(UUID id) {
    productRepository.delete(findOrThrow(id));
  }

  private Product findOrThrow(UUID id) {
    return productRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("Product not found with id: " + id));
  }

  private ProductResponse toResponse(Product product) {
    return new ProductResponse(
        product.getId(),
        product.getTitle(),
        product.getPrice(),
        product.getDescription(),
        product.getCategory(),
        product.getCreatedBy(),
        product.getCreatedAt(),
        product.getUpdatedAt());
  }
}
