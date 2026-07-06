package com.bts.task.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "products")
public class Product {
  @Id
  @GeneratedValue
  @Getter
  private UUID id;

  @Column(name = "title", nullable = false)
  @Getter
  @Setter
  private String title;

  @Column(name = "price", nullable = false)
  @Getter
  @Setter
  private BigDecimal price;

  @Column(name = "description", columnDefinition = "TEXT", nullable = false)
  @Getter
  @Setter
  private String description;

  @Column(name = "category", nullable = false)
  @Getter
  @Setter
  private String category;

  @Column(name = "created_at", nullable = false, updatable = false)
  @Getter
  @Setter
  private Instant createdAt;

  // use String instead relation of user for audit trail if user deleted (on
  // delete cascade)
  @Column(name = "created_by", nullable = false)
  @Getter
  @Setter
  private String createdBy;

  @Column(name = "created_by_id", nullable = false)
  @Getter
  @Setter
  private UUID createdById;

  @Column(name = "updated_at", nullable = false)
  @Getter
  @Setter
  private Instant updatedAt;

  // use String instead relation of user for audit trail if user deleted (on
  // delete cascade)
  @Column(name = "updated_by", nullable = false)
  @Getter
  @Setter
  private String updatedBy;

  @Column(name = "updated_by_id", nullable = false)
  @Getter
  @Setter
  private UUID updatedById;

  // private List<Image> images;
}
