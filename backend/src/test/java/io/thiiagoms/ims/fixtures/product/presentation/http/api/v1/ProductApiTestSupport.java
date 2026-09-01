package io.thiiagoms.ims.fixtures.product.presentation.http.api.v1;

import io.thiiagoms.ims.fixtures.category.presentation.http.api.v1.CategoryApiTestSupport;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.test.web.servlet.MockMvc;

public abstract class ProductApiTestSupport extends CategoryApiTestSupport {
  protected static final String PRODUCT_ENDPOINT = "/api/products";

  protected ProductApiTestSupport(MockMvc mockMvc) {
    super(mockMvc);
  }

  protected ProductRequest productRequest(String title, List<String> categoryIds) {
    return new ProductRequest(
        title,
        "Ergonomic office chair",
        "https://example.com/chair.png",
        new BigDecimal("499.90"),
        10,
        List.copyOf(categoryIds),
        LocalDateTime.of(2100, 1, 1, 0, 0));
  }

  public record ProductRequest(
      String title,
      String description,
      String imageUrl,
      BigDecimal price,
      Integer stockQuantity,
      List<String> categoryIds,
      LocalDateTime expiryDate) {}

  protected List<String> createProductCategories(String token) throws Exception {
    return List.of(
        createCategory(token, "Office", "Office products"),
        createCategory(token, "Warehouse", "Warehouse products"),
        createCategory(token, "General", "General products"));
  }
}
