package com.bts.task.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bts.task.model.User;

public interface UserRepository extends JpaRepository<User, UUID> {
  public boolean existsByUsername(String username);

  public Optional<User> findByUsername(String username);
}
