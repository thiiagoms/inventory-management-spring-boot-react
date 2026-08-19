package io.thiiagoms.ims.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "suppliers")
public class Supplier {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank(message = "Name is required.")
  @Column(name = "name", nullable = false)
  private String name;

  @NotBlank(message = "Contact is required.")
  @Column(name = "contact", nullable = false)
  private String contact;

  @NotBlank(message = "Address is required.")
  @Column(name = "address", nullable = false)
  private String address;

  @Column(name = "created_at")
  private final LocalDateTime createdAt = LocalDateTime.now();

  @Override
  public String toString() {
    return """
                {
                    "id": "%d",
                    "name": "%s",
                    "contact": "%s",
                    "address": "%s",
                    "created_at": "%s"
                }
                """
        .formatted(this.id, this.name, this.contact, this.address, this.createdAt.toString());
  }
}
