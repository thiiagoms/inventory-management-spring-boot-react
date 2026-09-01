package io.thiiagoms.ims.product.presentation.http.api.v1;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.thiiagoms.ims.fixtures.product.presentation.http.api.v1.ProductApiTestSupport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

class RetrieveProductsApiTest extends ProductApiTestSupport {
  @Autowired
  RetrieveProductsApiTest(MockMvc mockMvc) {
    super(mockMvc);
  }

  @Test
  void itRetrievesTheFirstProductPage() throws Exception {
    String token = authenticate();
    createProducts(token);

    getJson(PRODUCT_ENDPOINT + "?page=0&size=2", token)
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content.length()").value(2))
        .andExpect(jsonPath("$.content[0].title").value("Meeting Chair"))
        .andExpect(jsonPath("$.content[1].title").value("Office Chair"))
        .andExpect(jsonPath("$.page").value(0))
        .andExpect(jsonPath("$.size").value(2))
        .andExpect(jsonPath("$.totalElements").value(3))
        .andExpect(jsonPath("$.totalPages").value(2))
        .andExpect(jsonPath("$.first").value(true))
        .andExpect(jsonPath("$.last").value(false));
  }

  @Test
  void itRetrievesTheLastProductPage() throws Exception {
    String token = authenticate();
    createProducts(token);

    getJson(PRODUCT_ENDPOINT + "?page=1&size=2", token)
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content.length()").value(1))
        .andExpect(jsonPath("$.content[0].title").value("Warehouse Chair"))
        .andExpect(jsonPath("$.page").value(1))
        .andExpect(jsonPath("$.first").value(false))
        .andExpect(jsonPath("$.last").value(true));
  }

  @Test
  void itRetrievesAnEmptyProductPage() throws Exception {
    String token = authenticate();

    getJson(PRODUCT_ENDPOINT, token)
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content.length()").value(0))
        .andExpect(jsonPath("$.page").value(0))
        .andExpect(jsonPath("$.size").value(20))
        .andExpect(jsonPath("$.totalElements").value(0))
        .andExpect(jsonPath("$.totalPages").value(0))
        .andExpect(jsonPath("$.first").value(true))
        .andExpect(jsonPath("$.last").value(true));
  }

  @Test
  void itRejectsANegativePage() throws Exception {
    String token = authenticate();

    getJson(PRODUCT_ENDPOINT + "?page=-1&size=20", token)
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.error").value("validation_failed"))
        .andExpect(jsonPath("$.field").value("page"));
  }

  @Test
  void itRejectsAnUnsupportedPageSize() throws Exception {
    String token = authenticate();

    getJson(PRODUCT_ENDPOINT + "?page=0&size=101", token)
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.error").value("validation_failed"))
        .andExpect(jsonPath("$.field").value("size"));
  }

  @Test
  void itRequiresAuthentication() throws Exception {
    getJson(PRODUCT_ENDPOINT).andExpect(status().isUnauthorized());
  }

  private void createProducts(String token) throws Exception {
    var categoryIds = createProductCategories(token);
    postJsonAndReturnId(PRODUCT_ENDPOINT, productRequest("Warehouse Chair", categoryIds), token);
    postJsonAndReturnId(PRODUCT_ENDPOINT, productRequest("Office Chair", categoryIds), token);
    postJsonAndReturnId(PRODUCT_ENDPOINT, productRequest("Meeting Chair", categoryIds), token);
  }
}
