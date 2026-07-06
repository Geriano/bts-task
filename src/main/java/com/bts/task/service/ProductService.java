package com.bts.task.service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
import com.bts.task.model.Image;
import com.bts.task.model.Product;
import com.bts.task.model.User;
import com.bts.task.repository.ImageRepository;
import com.bts.task.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {
  @Autowired
  private final ProductRepository productRepository;

  @Autowired
  private final ImageRepository imageRepository;

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

    product = productRepository.save(product);
    saveImages(product.getId(), req.images(), now);

    return toResponse(product);
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

    product = productRepository.save(product);
    imageRepository.deleteAll(imageRepository.findAllByImageableId(product.getId()));
    saveImages(product.getId(), req.images(), Instant.now());

    return toResponse(product);
  }

  @Transactional()
  public void delete(UUID id) {
    Product product = findOrThrow(id);
    imageRepository.deleteAll(imageRepository.findAllByImageableId(product.getId()));
    productRepository.delete(product);
  }

  private void saveImages(UUID imageableId, List<String> urls, Instant now) {
    if (urls == null || urls.isEmpty()) {
      return;
    }
    List<Image> images = urls.stream()
        .map(url -> {
          Image img = new Image();
          img.setImageableId(imageableId);
          img.setUrl(url);
          img.setCreatedAt(now);
          img.setUpdatedAt(now);
          return img;
        })
        .collect(Collectors.toList());
    imageRepository.saveAll(images);
  }

  private Product findOrThrow(UUID id) {
    return productRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("Product not found with id: " + id));
  }

  private ProductResponse toResponse(Product product) {
    List<String> images = imageRepository.findAllByImageableId(product.getId())
        .stream()
        .map(img -> img.getUrl())
        .collect(Collectors.toList());

    return new ProductResponse(
        product.getId(),
        product.getTitle(),
        product.getPrice(),
        product.getDescription(),
        product.getCategory(),
        product.getCreatedBy(),
        product.getCreatedAt(),
        product.getUpdatedAt(),
        images);
  }
}
