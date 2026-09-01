package io.thiiagoms.ims.product.presentation.http.api.v1;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.thiiagoms.ims.fixtures.product.presentation.http.api.v1.ProductApiTestSupport;
import io.thiiagoms.ims.product.infrastructure.persistence.repository.ProductJpaRepository;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

public class DestroyProductApiTest extends ProductApiTestSupport {

  private final ProductJpaRepository repository;

  @Autowired
  DestroyProductApiTest(MockMvc mockMvc, ProductJpaRepository repository) {
    super(mockMvc);
    this.repository = repository;
  }

  @Test
  void itDestroysAProduct() throws Exception {
    String token = authenticate();
    String categoryId = createCategory(token, "Office", "Office products");
    String productId =
        postJsonAndReturnId(PRODUCT_ENDPOINT, productRequest("Office Chair", categoryId), token);

    deleteJson(PRODUCT_ENDPOINT + "/" + productId, token).andExpect(status().isNoContent());

    assertThat(repository.findById(UUID.fromString(productId))).isEmpty();
  }

  @Test
  void itReturnsNotFoundForAnUnknownProduct() throws Exception {
    String token = authenticate();

    deleteJson(PRODUCT_ENDPOINT + "/3780baf2-deed-448d-a763-ce7b06efd394", token)
        .andExpect(status().isNotFound());
  }
}
