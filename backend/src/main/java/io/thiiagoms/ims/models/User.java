package io.thiiagoms.ims.models;

import io.thiiagoms.ims.enums.user.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

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
  @Enumerated(EnumType.STRING)
  private Role role;

  @OneToMany(mappedBy = "user")
  private List<Transaction> transactions;

  @Column(name = "created_at")
  private final LocalDateTime createdAt = LocalDateTime.now();

  @Override
  public String toString() {
    return ("{%n"
            + "    \"id\": \"%d\",%n"
            + "    \"name\": \"%s\",%n"
            + "    \"email\": \"%s\",%n"
            + "    \"phone\": \"%s\",%n"
            + "    \"role\": \"%s\",%n"
            + "    \"created_at\": \"%s\"%n"
            + "}%n")
        .formatted(
            this.id,
            this.name,
            this.email,
            this.phone,
            this.role.toString(),
            this.createdAt.toString());
  }
}
