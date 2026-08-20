package io.thiiagoms.ims.user.infrastructure.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import io.thiiagoms.ims.user.infrastructure.persistence.model.UserJpa;

public interface UserJpaRepository extends JpaRepository<UserJpa, String> {

    Optional<UserJpa> findByEmail(String email);

    Optional<UserJpa> findByPhone(String phone);
}
