package io.thiiagoms.ims.product.presentation.http.api.v1.shared;

import io.thiiagoms.ims.product.application.dto.ProductOutput;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record ProductResponse(
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
  public ProductResponse {
    categoryIds = List.copyOf(categoryIds);
  }

  public static ProductResponse from(ProductOutput output) {
    return new ProductResponse(
        output.id(),
        output.title(),
        output.description(),
        output.sku(),
        output.imageUrl(),
        output.price(),
        output.stockQuantity(),
        output.categoryIds(),
        output.supplierId(),
        output.expiryDate());
  }
}
