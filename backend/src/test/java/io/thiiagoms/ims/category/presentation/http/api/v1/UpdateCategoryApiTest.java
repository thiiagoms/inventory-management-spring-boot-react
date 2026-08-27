package io.thiiagoms.ims.category.presentation.http.api.v1;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.thiiagoms.ims.fixtures.category.presentation.http.api.v1.CategoryApiTestSupport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

class UpdateCategoryApiTest extends CategoryApiTestSupport {
  @Autowired
  UpdateCategoryApiTest(MockMvc mockMvc) {
    super(mockMvc);
  }

  @Test
  void itUpdatesACategory() throws Exception {
    String token = authenticate();
    String id = createCategory(token, "Office", "Office products");

    patchJson(ENDPOINT + "/" + id, new CategoryRequest("home office", "Products for home"), token)
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.title").value("Home Office"))
        .andExpect(jsonPath("$.description").value("products for home"));
  }

  @Test
  void itRejectsATitleOwnedByAnotherCategory() throws Exception {
    String token = authenticate();
    createCategory(token, "Office", "Office products");
    String kitchenId = createCategory(token, "Kitchen", "Kitchen products");

    patchJson(ENDPOINT + "/" + kitchenId, new CategoryRequest("office", null), token)
        .andExpect(status().isConflict())
        .andExpect(jsonPath("$.field").value("title"));
  }

  @Test
  void itRejectsAnUpdateWithoutChanges() throws Exception {
    String token = authenticate();
    String id = createCategory(token, "Office", "Office products");

    patchJson(ENDPOINT + "/" + id, new CategoryRequest(null, null), token)
        .andExpect(status().isUnprocessableContent())
        .andExpect(jsonPath("$.error").value("resource_not_changed"));
  }
}
