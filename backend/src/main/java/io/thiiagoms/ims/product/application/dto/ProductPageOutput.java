package io.thiiagoms.ims.product.application.dto;

import io.thiiagoms.ims.product.domain.Product;
import io.thiiagoms.ims.shared.domain.pagination.Page;
import java.util.List;

public record ProductPageOutput(
    List<ProductOutput> content,
    int page,
    int size,
    long totalElements,
    int totalPages,
    boolean first,
    boolean last) {
  public ProductPageOutput {
    content = List.copyOf(content);
  }

  public static ProductPageOutput from(Page<Product> products) {
    return new ProductPageOutput(
        products.content().stream().map(ProductOutput::from).toList(),
        products.page(),
        products.size(),
        products.totalElements(),
        products.totalPages(),
        products.first(),
        products.last());
  }
}
