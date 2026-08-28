package io.thiiagoms.ims.category.presentation.http.api.v1;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.thiiagoms.ims.fixtures.category.presentation.http.api.v1.CategoryApiTestSupport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

class FindCategoryApiTest extends CategoryApiTestSupport {
  @Autowired
  FindCategoryApiTest(MockMvc mockMvc) {
    super(mockMvc);
  }

  @Test
  void itFindsACategoryById() throws Exception {
    String token = authenticate();
    String id = createCategory(token, "Office", "Office products");

    getJson(ENDPOINT + "/" + id, token)
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(id))
        .andExpect(jsonPath("$.title").value("Office"));
  }

  @Test
  void itListsCategories() throws Exception {
    String token = authenticate();
    String id = createCategory(token, "Office", "Office products");

    getJson(ENDPOINT, token).andExpect(status().isOk()).andExpect(jsonPath("$[0].id").value(id));
  }

  @Test
  void itReturnsNotFoundForAnUnknownCategory() throws Exception {
    String token = authenticate();

    getJson(ENDPOINT + "/3780baf2-deed-448d-a763-ce7b06efd394", token)
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.field").value("id"));
  }
}
