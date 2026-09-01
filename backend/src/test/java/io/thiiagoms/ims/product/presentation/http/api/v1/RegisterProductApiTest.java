package io.thiiagoms.ims.product.presentation.http.api.v1;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.thiiagoms.ims.fixtures.product.presentation.http.api.v1.ProductApiTestSupport;
import io.thiiagoms.ims.product.infrastructure.persistence.repository.ProductJpaRepository;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

public class RegisterProductApiTest extends ProductApiTestSupport {

  private final ProductJpaRepository repository;

  @Autowired
  RegisterProductApiTest(MockMvc mockMvc, ProductJpaRepository repository) {
    super(mockMvc);
    this.repository = repository;
  }

  @Test
  void itRegistersAnAuthenticatedProduct() throws Exception {
    String token = authenticate();
    List<String> categoryIds = createProductCategories(token);

    String response =
        postJson(PRODUCT_ENDPOINT, productRequest("office chair", categoryIds), token)
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.title").value("Office Chair"))
            .andExpect(jsonPath("$.sku").value(org.hamcrest.Matchers.startsWith("office-chair-")))
            .andExpect(jsonPath("$.categoryIds.length()").value(3))
            .andReturn()
            .getResponse()
            .getContentAsString();

    assertThat(repository.findById(UUID.fromString(readId(response)))).isPresent();
  }

  @Test
  void itRequiresAuthentication() throws Exception {
    postJson(
            PRODUCT_ENDPOINT,
            productRequest(
                "Office Chair",
                List.of(
                    "430e7bc1-59b9-472e-ae21-3cd90cde7caa",
                    "3780baf2-deed-448d-a763-ce7b06efd394",
                    "baa86496-638f-4beb-bc03-de2f7589ad63")))
        .andExpect(status().isUnauthorized());
  }

  @Test
  void itRejectsANormalizedDuplicateTitle() throws Exception {
    String token = authenticate();
    List<String> categoryIds = createProductCategories(token);
    postJsonAndReturnId(PRODUCT_ENDPOINT, productRequest("Office Chair", categoryIds), token);

    postJson(PRODUCT_ENDPOINT, productRequest("  office chair ", categoryIds), token)
        .andExpect(status().isConflict())
        .andExpect(jsonPath("$.field").value("title"));
  }

  @Test
  void itRejectsFewerThanThreeCategories() throws Exception {
    String token = authenticate();
    List<String> categoryIds = createProductCategories(token);

    postJson(PRODUCT_ENDPOINT, productRequest("Office Chair", categoryIds.subList(0, 2)), token)
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.field").value("categoryIds"));
  }

  @Test
  void itRejectsDuplicateCategories() throws Exception {
    String token = authenticate();
    List<String> categoryIds = createProductCategories(token);

    postJson(
            PRODUCT_ENDPOINT,
            productRequest(
                "Office Chair",
                List.of(categoryIds.get(0), categoryIds.get(1), categoryIds.get(1))),
            token)
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.field").value("categoryIds"));
  }

  @Test
  void itRejectsAnUnknownCategory() throws Exception {
    String token = authenticate();
    List<String> categoryIds = createProductCategories(token);

    postJson(
            PRODUCT_ENDPOINT,
            productRequest(
                "Office Chair",
                List.of(
                    categoryIds.get(0),
                    categoryIds.get(1),
                    "e312d572-b37c-4497-8b68-13b59db24ef5")),
            token)
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.field").value("id"));
  }
}
