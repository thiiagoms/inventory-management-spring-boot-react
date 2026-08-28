package io.thiiagoms.ims.product.application.dto;

import io.thiiagoms.ims.product.domain.Product;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProductOutput(
    String id,
    String title,
    String description,
    String sku,
    String imageUrl,
    BigDecimal price,
    Integer stockQuantity,
    String categoryId,
    LocalDateTime expiryDate) {
  public static ProductOutput from(Product product) {
    return new ProductOutput(
        product.id().value(),
        product.title().value(),
        product.description().value(),
        product.sku().value(),
        product.imageUrl().value(),
        product.price().value(),
        product.stockQuantity().value(),
        product.categoryId().value(),
        product.expiryDate().value());
  }
}
