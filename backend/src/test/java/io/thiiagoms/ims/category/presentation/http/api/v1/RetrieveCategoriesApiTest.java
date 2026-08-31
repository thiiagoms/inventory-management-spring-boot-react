package io.thiiagoms.ims.category.presentation.http.api.v1;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.thiiagoms.ims.fixtures.category.presentation.http.api.v1.CategoryApiTestSupport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

class RetrieveCategoriesApiTest extends CategoryApiTestSupport {
  @Autowired
  RetrieveCategoriesApiTest(MockMvc mockMvc) {
    super(mockMvc);
  }

  @Test
  void itRetrievesTheFirstCategoryPage() throws Exception {
    String token = authenticate();
    createCategories(token);

    getJson(ENDPOINT + "?page=0&size=2", token)
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content.length()").value(2))
        .andExpect(jsonPath("$.content[0].title").value("Kitchen"))
        .andExpect(jsonPath("$.content[1].title").value("Office"))
        .andExpect(jsonPath("$.page").value(0))
        .andExpect(jsonPath("$.size").value(2))
        .andExpect(jsonPath("$.totalElements").value(3))
        .andExpect(jsonPath("$.totalPages").value(2))
        .andExpect(jsonPath("$.first").value(true))
        .andExpect(jsonPath("$.last").value(false));
  }

  @Test
  void itRetrievesTheLastCategoryPage() throws Exception {
    String token = authenticate();
    createCategories(token);

    getJson(ENDPOINT + "?page=1&size=2", token)
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content.length()").value(1))
        .andExpect(jsonPath("$.content[0].title").value("Warehouse"))
        .andExpect(jsonPath("$.first").value(false))
        .andExpect(jsonPath("$.last").value(true));
  }

  @Test
  void itRetrievesAnEmptyCategoryPage() throws Exception {
    String token = authenticate();

    getJson(ENDPOINT, token)
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
  void itRejectsInvalidPagination() throws Exception {
    String token = authenticate();

    getJson(ENDPOINT + "?page=-1&size=101", token)
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.error").value("validation_failed"))
        .andExpect(jsonPath("$.field").value("page"));
  }

  @Test
  void itRequiresAuthentication() throws Exception {
    getJson(ENDPOINT).andExpect(status().isUnauthorized());
  }

  private void createCategories(String token) throws Exception {
    createCategory(token, "Warehouse", "Warehouse products");
    createCategory(token, "Office", "Office products");
    createCategory(token, "Kitchen", "Kitchen products");
  }
}
