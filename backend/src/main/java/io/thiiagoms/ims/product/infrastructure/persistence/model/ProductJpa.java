package io.thiiagoms.ims.product.infrastructure.persistence.model;

import io.thiiagoms.ims.category.infrastructure.persistence.model.CategoryJpa;
import io.thiiagoms.ims.shared.infrastructure.persistence.model.BaseJpaEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@Entity
@Table(name = "products")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductJpa extends BaseJpaEntity {

  @Column(name = "title", nullable = false, unique = true, length = 250)
  private String title;

  @Column(name = "description", nullable = false)
  private String description;

  @Column(name = "sku", nullable = false, unique = true)
  private String sku;

  @Column(name = "image_url", nullable = false)
  private String imageUrl;

  @Column(name = "price", nullable = false)
  private BigDecimal price;

  @Column(name = "stock_quantity", nullable = false)
  private Integer stockQuantity;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "category_id", nullable = false)
  private CategoryJpa category;

  @Column(name = "expiry_date", nullable = false)
  private LocalDateTime expiryDate;
}
