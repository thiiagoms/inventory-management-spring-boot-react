package io.thiiagoms.ims.product.presentation.http.api.v1;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.thiiagoms.ims.fixtures.product.presentation.http.api.v1.ProductApiTestSupport;
import io.thiiagoms.ims.product.infrastructure.persistence.repository.ProductJpaRepository;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

class RegisterProductApiTest extends ProductApiTestSupport {
  private final ProductJpaRepository repository;

  @Autowired
  RegisterProductApiTest(MockMvc mockMvc, ProductJpaRepository repository) {
    super(mockMvc);
    this.repository = repository;
  }

  @Test
  void itRegistersAnAuthenticatedProduct() throws Exception {
    String token = authenticate();
    String categoryId = createCategory(token, "Office", "Office products");

    String response =
        postJson(PRODUCT_ENDPOINT, productRequest("office chair", "CHAIR-001", categoryId), token)
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.title").value("Office Chair"))
            .andExpect(jsonPath("$.categoryId").value(categoryId))
            .andReturn()
            .getResponse()
            .getContentAsString();

    assertThat(repository.findById(UUID.fromString(readId(response)))).isPresent();
  }

  @Test
  void itRequiresAuthentication() throws Exception {
    postJson(
            PRODUCT_ENDPOINT,
            productRequest("Office Chair", "CHAIR-001", "430e7bc1-59b9-472e-ae21-3cd90cde7caa"))
        .andExpect(status().isUnauthorized());
  }

  @Test
  void itRejectsANormalizedDuplicateTitle() throws Exception {
    String token = authenticate();
    String categoryId = createCategory(token, "Office", "Office products");
    postJsonAndReturnId(
        PRODUCT_ENDPOINT, productRequest("Office Chair", "CHAIR-001", categoryId), token);

    postJson(PRODUCT_ENDPOINT, productRequest("  office chair ", "CHAIR-002", categoryId), token)
        .andExpect(status().isConflict())
        .andExpect(jsonPath("$.field").value("title"));
  }

  @Test
  void itRejectsANormalizedDuplicateSku() throws Exception {
    String token = authenticate();
    String categoryId = createCategory(token, "Office", "Office products");
    postJsonAndReturnId(
        PRODUCT_ENDPOINT, productRequest("Office Chair", "CHAIR-001", categoryId), token);

    postJson(PRODUCT_ENDPOINT, productRequest("Meeting Chair", " chair-001 ", categoryId), token)
        .andExpect(status().isConflict())
        .andExpect(jsonPath("$.field").value("sku"));
  }
}
