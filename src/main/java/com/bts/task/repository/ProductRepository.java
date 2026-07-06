package com.bts.task.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bts.task.model.Product;

public interface ProductRepository extends JpaRepository<Product, UUID> {
  @Query("""
      SELECT p FROM Product p WHERE (
          :keyword IS NULL OR
          p.title ILIKE CONCAT('%', :keyword, '%') OR
          p.description ILIKE CONCAT('%', :keyword ,'%')
      )
      AND (
        :category IS NULL OR
        p.category = :category
      )
      """)
  Page<Product> search(@Param("keyword") String keyword, @Param("category") String category, Pageable pageable);
}
