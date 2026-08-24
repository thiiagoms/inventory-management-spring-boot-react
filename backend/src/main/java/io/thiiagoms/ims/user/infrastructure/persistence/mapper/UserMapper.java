package io.thiiagoms.ims.user.infrastructure.persistence.mapper;

import io.thiiagoms.ims.shared.domain.valueobject.Id;
import io.thiiagoms.ims.shared.domain.valueobject.Timestamp;
import io.thiiagoms.ims.user.domain.Role;
import io.thiiagoms.ims.user.domain.User;
import io.thiiagoms.ims.user.domain.valueobject.Email;
import io.thiiagoms.ims.user.domain.valueobject.Name;
import io.thiiagoms.ims.user.domain.valueobject.PasswordHash;
import io.thiiagoms.ims.user.domain.valueobject.Phone;
import io.thiiagoms.ims.user.infrastructure.persistence.model.UserJpa;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public class UserMapper {

  public static UserJpa toPersistence(User user) {
    return UserJpa.builder()
        .id(UUID.fromString(user.id().value()))
        .name(user.name().value())
        .email(user.email().value())
        .password(user.password().value())
        .phone(user.phone().value())
        .role(user.role().name())
        .lastLoginAt(
            user.lastLoginAt().map(timestamp -> Instant.parse(timestamp.value())).orElse(null))
        .build();
  }

  public static User toDomain(UserJpa user) {
    return User.rehydrate(
        new Id(user.getId().toString()),
        new Name(user.getName()),
        new Email(user.getEmail()),
        new Phone(user.getPhone()),
        new PasswordHash(user.getPassword()),
        Role.valueOf(user.getRole()),
        Optional.ofNullable(user.getLastLoginAt())
            .map(lastLoginAt -> new Timestamp(lastLoginAt.toString())));
  }
}
