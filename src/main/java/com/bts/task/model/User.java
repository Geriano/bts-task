package com.bts.task.model;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
public class User implements UserDetails {
  @Id
  @GeneratedValue
  @Getter
  @Setter
  private UUID id;

  @Column(name = "name", nullable = false)
  @Getter
  @Setter
  private String name;

  @Column(name = "username", nullable = false, unique = true)
  @Getter
  @Setter
  private String username;

  @Column(name = "password", nullable = false)
  @Getter
  @Setter
  private String password;

  @Column(name = "created_at", nullable = false, updatable = false)
  @Getter
  @Setter
  private Instant createdAt;

  @Column(name = "updated_at", nullable = false)
  @Getter
  @Setter
  private Instant updatedAt;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority("ADMIN"));
  }
}
