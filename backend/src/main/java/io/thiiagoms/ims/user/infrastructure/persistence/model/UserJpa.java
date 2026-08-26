package io.thiiagoms.ims.user.infrastructure.persistence.model;

import io.thiiagoms.ims.models.Transaction;
import io.thiiagoms.ims.shared.infrastructure.persistence.model.BaseJpaEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import java.time.Instant;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class UserJpa extends BaseJpaEntity {

  @NotBlank(message = "Name is required.")
  @Column(name = "name", nullable = false)
  private String name;

  @NotBlank(message = "E-mail is required.")
  @Column(name = "email", nullable = false, unique = true)
  private String email;

  @NotBlank(message = "Password is required.")
  @Column(name = "password", nullable = false)
  private String password;

  @NotBlank(message = "Phone is required.")
  @Column(name = "phone", nullable = false, unique = true)
  private String phone;

  @NotBlank(message = "Role is required.")
  @Column(name = "role", nullable = false)
  private String role;

  @OneToMany(mappedBy = "user")
  private List<Transaction> transactions;

  @Column(name = "last_login_at")
  private Instant lastLoginAt;

  @Override
  public String toString() {
    return """
        {%n\
          "id": "%s",%n\
          "name": "%s",%n\
          "email": "%s",%n\
          "phone": "%s",%n\
          "role": "%s",%n\
          "created_at": "%s",%n\
          "last_login_at": "%s"%n\
        }%n\
        """
        .formatted(
            this.getId(),
            this.name,
            this.email,
            this.phone,
            this.role,
            this.getCreatedAt(),
            this.lastLoginAt);
  }
}
