package com.bts.task.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bts.task.model.Image;

public interface ImageRepository extends JpaRepository<Image, UUID> {
  public List<Image> findAllByImageableId(UUID imageableId);
}
