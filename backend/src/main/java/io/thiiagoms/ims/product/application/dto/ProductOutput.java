package io.thiiagoms.ims.product.application.dto;

import io.thiiagoms.ims.product.domain.Product;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record ProductOutput(
    String id,
    String title,
    String description,
    String sku,
    String imageUrl,
    BigDecimal price,
    Integer stockQuantity,
    List<String> categoryIds,
    String supplierId,
    LocalDateTime expiryDate) {
  public ProductOutput {
    categoryIds = List.copyOf(categoryIds);
  }

  public static ProductOutput from(Product product) {
    return new ProductOutput(
        product.id().value(),
        product.title().value(),
        product.description().value(),
        product.sku().value(),
        product.imageUrl().value(),
        product.price().value(),
        product.stockQuantity().value(),
        product.categoryIds().values().stream().map(categoryId -> categoryId.value()).toList(),
        product.supplierId().value(),
        product.expiryDate().value());
  }
}
