package io.thiiagoms.ims.product.presentation.http.api.v1;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.thiiagoms.ims.fixtures.product.presentation.http.api.v1.ProductApiTestSupport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

class RetrieveProductApiTest extends ProductApiTestSupport {

  @Autowired
  RetrieveProductApiTest(MockMvc mockMvc) {
    super(mockMvc);
  }

  @Test
  void itRetrievesAProductById() throws Exception {
    String token = authenticate();
    String categoryId = createCategory(token, "Office", "Office products");
    String productId =
        postJsonAndReturnId(PRODUCT_ENDPOINT, productRequest("Office Chair", categoryId), token);

    getJson(PRODUCT_ENDPOINT + "/" + productId, token)
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(productId))
        .andExpect(jsonPath("$.title").value("Office Chair"))
        .andExpect(jsonPath("$.description").value("Ergonomic office chair"))
        .andExpect(jsonPath("$.sku").value(org.hamcrest.Matchers.startsWith("office-chair-")))
        .andExpect(jsonPath("$.imageUrl").value("https://example.com/chair.png"))
        .andExpect(jsonPath("$.price").value(499.90))
        .andExpect(jsonPath("$.stockQuantity").value(10))
        .andExpect(jsonPath("$.categoryId").value(categoryId));
  }

  @Test
  void itReturnsNotFoundForAnUnknownProduct() throws Exception {
    String token = authenticate();

    getJson(PRODUCT_ENDPOINT + "/3780baf2-deed-448d-a763-ce7b06efd394", token)
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.status").value(404))
        .andExpect(jsonPath("$.error").value("resource_not_found"))
        .andExpect(jsonPath("$.field").value("id"));
  }

  @Test
  void itRequiresAuthentication() throws Exception {
    getJson(PRODUCT_ENDPOINT + "/3780baf2-deed-448d-a763-ce7b06efd394")
        .andExpect(status().isUnauthorized());
  }
}
