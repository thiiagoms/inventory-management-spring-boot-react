package io.thiiagoms.ims.product.presentation.http.api.v1.shared;

import io.thiiagoms.ims.product.application.dto.ProductOutput;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProductResponse(
    String id,
    String title,
    String description,
    String sku,
    String imageUrl,
    BigDecimal price,
    Integer stockQuantity,
    String categoryId,
    LocalDateTime expiryDate) {
  public static ProductResponse from(ProductOutput output) {
    return new ProductResponse(
        output.id(),
        output.title(),
        output.description(),
        output.sku(),
        output.imageUrl(),
        output.price(),
        output.stockQuantity(),
        output.categoryId(),
        output.expiryDate());
  }
}
