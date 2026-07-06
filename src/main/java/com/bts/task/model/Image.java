package com.bts.task.model;

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
@Table(name = "images")
public class Image {
  @Id
  @GeneratedValue
  @Getter
  private UUID id;

  // little polymorphic
  @Column(name = "imageable_id", nullable = false)
  @Getter
  @Setter
  private UUID imageableId;

  @Column(name = "url")
  @Getter
  @Setter
  private String url;

  @Column(name = "created_at", nullable = false, updatable = false)
  @Getter
  @Setter
  private Instant createdAt;

  @Column(name = "updated_at", nullable = false)
  @Getter
  @Setter
  private Instant updatedAt;
}
