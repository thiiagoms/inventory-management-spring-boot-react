package io.thiiagoms.ims.fixtures.product.presentation.http.api.v1;

import io.thiiagoms.ims.fixtures.category.presentation.http.api.v1.CategoryApiTestSupport;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.test.web.servlet.MockMvc;

public abstract class ProductApiTestSupport extends CategoryApiTestSupport {
  protected static final String PRODUCT_ENDPOINT = "/api/products";
  private String supplierId = "f1147c86-f31d-4683-9b86-46a665fed044";

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
        supplierId,
        LocalDateTime.of(2100, 1, 1, 0, 0));
  }

  public record ProductRequest(
      String title,
      String description,
      String imageUrl,
      BigDecimal price,
      Integer stockQuantity,
      List<String> categoryIds,
      String supplierId,
      LocalDateTime expiryDate) {}

  protected List<String> createProductCategories(String token) throws Exception {
    supplierId =
        postJsonAndReturnId(
            "/api/suppliers",
            new SupplierRequest(
                "Acme Supplies Ltda", "11222333000181", "Praça da Sé, São Paulo - SP, 01001-000"),
            token);
    return List.of(
        createCategory(token, "Office", "Office products"),
        createCategory(token, "Warehouse", "Warehouse products"),
        createCategory(token, "General", "General products"));
  }

  private record SupplierRequest(String socialName, String cnpj, String address) {}
}
