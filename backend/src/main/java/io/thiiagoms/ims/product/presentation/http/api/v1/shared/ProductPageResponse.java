package io.thiiagoms.ims.product.presentation.http.api.v1.shared;

import io.thiiagoms.ims.product.application.dto.ProductPageOutput;
import java.util.List;

public record ProductPageResponse(
    List<ProductResponse> content,
    int page,
    int size,
    long totalElements,
    int totalPages,
    boolean first,
    boolean last) {
  public ProductPageResponse {
    content = List.copyOf(content);
  }

  public static ProductPageResponse from(ProductPageOutput output) {
    return new ProductPageResponse(
        output.content().stream().map(ProductResponse::from).toList(),
        output.page(),
        output.size(),
        output.totalElements(),
        output.totalPages(),
        output.first(),
        output.last());
  }
}
