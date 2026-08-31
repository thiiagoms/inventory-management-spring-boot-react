package io.thiiagoms.ims.fixtures.product.presentation.http.api.v1;

import io.thiiagoms.ims.fixtures.category.presentation.http.api.v1.CategoryApiTestSupport;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.springframework.test.web.servlet.MockMvc;

public abstract class ProductApiTestSupport extends CategoryApiTestSupport {
  protected static final String PRODUCT_ENDPOINT = "/api/products";

  protected ProductApiTestSupport(MockMvc mockMvc) {
    super(mockMvc);
  }

  protected ProductRequest productRequest(String title, String categoryId) {
    return new ProductRequest(
        title,
        "Ergonomic office chair",
        "https://example.com/chair.png",
        new BigDecimal("499.90"),
        10,
        categoryId,
        LocalDateTime.of(2100, 1, 1, 0, 0));
  }

  public record ProductRequest(
      String title,
      String description,
      String imageUrl,
      BigDecimal price,
      Integer stockQuantity,
      String categoryId,
      LocalDateTime expiryDate) {}
}
