package io.thiiagoms.ims.user.infrastructure.persistence.repository;

import io.thiiagoms.ims.user.infrastructure.persistence.model.UserJpa;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<UserJpa, UUID> {

  Optional<UserJpa> findByEmail(String email);

  Optional<UserJpa> findByPhone(String phone);
}
