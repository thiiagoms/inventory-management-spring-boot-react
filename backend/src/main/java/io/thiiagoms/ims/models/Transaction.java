package io.thiiagoms.ims.models;

import io.thiiagoms.ims.enums.transaction.Status;
import io.thiiagoms.ims.enums.transaction.Type;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "transactions")
@Data
@Builder
public class Transaction {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank(message = "Total of Products is required.")
  @Positive(message = "Total of products must be a positive value.")
  @Column(name = "total_products", nullable = false)
  private Integer totalProducts;

  @NotBlank(message = "Price of Products is required.")
  @Positive(message = "Price of products must be a positive value.")
  @Column(name = "total_price", nullable = false)
  private BigDecimal totalPrice;

  @Enumerated(EnumType.STRING)
  @Column(name = "transaction_type", nullable = false)
  private Type transactionType;

  @Enumerated(EnumType.STRING)
  @Column(name = "transaction_status", nullable = false)
  private Status transactionStatus;

  @NotBlank(message = "Description is required.")
  @Column(name = "descritpion", nullable = false)
  private String description;

  @NotBlank(message = "Note is required.")
  @Column(name = "note", nullable = false)
  private String note;

  @Column(name = "created_at")
  private final LocalDateTime createdAt = LocalDateTime.now();

  @Column(name = "updated_at")
  private LocalDateTime updateAt;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "product_id")
  private Product product;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "supplier_id")
  private Supplier supplier;

  @Override
  public String toString() {
    return ("    \"id\": \"%d\",%n"
            + "    \"total_products\": \"%d\",%n"
            + "    \"total_prices\": \"%d\",%n"
            + "    \"transaction_type\": \"%s\",%n"
            + "    \"transaction_status\": \"%s\",%n"
            + "    \"descritpion\": \"%s\",%n"
            + "    \"note\": \"%s\",%n"
            + "    \"created_at\": \"%s\",%n"
            + "    \"updated_at\": \"%s\"%n")
        .formatted(
            this.id,
            this.totalProducts,
            this.totalPrice,
            this.transactionType,
            this.transactionStatus,
            this.description,
            this.note,
            this.createdAt.toString(),
            this.updateAt.toString());
  }
}
