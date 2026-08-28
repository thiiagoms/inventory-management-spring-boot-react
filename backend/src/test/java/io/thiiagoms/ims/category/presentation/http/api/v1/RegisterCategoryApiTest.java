package io.thiiagoms.ims.category.presentation.http.api.v1;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.thiiagoms.ims.category.infrastructure.persistence.repository.CategoryJpaRepository;
import io.thiiagoms.ims.fixtures.category.presentation.http.api.v1.CategoryApiTestSupport;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

class RegisterCategoryApiTest extends CategoryApiTestSupport {

  private final CategoryJpaRepository repository;

  @Autowired
  RegisterCategoryApiTest(MockMvc mockMvc, CategoryJpaRepository repository) {
    super(mockMvc);
    this.repository = repository;
  }

  @Test
  void itRegistersAnAuthenticatedCategory() throws Exception {
    String token = authenticate();
    var request = new CategoryRequest("office supplies", "Products for the office");

    String response =
        postJson(ENDPOINT, request, token)
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.title").value("Office Supplies"))
            .andExpect(jsonPath("$.description").value("products for the office"))
            .andReturn()
            .getResponse()
            .getContentAsString();

    assertThat(repository.findById(UUID.fromString(readId(response)))).isPresent();
  }

  @Test
  void itRequiresAuthentication() throws Exception {
    postJson(ENDPOINT, new CategoryRequest("Office", "Office products"))
        .andExpect(status().isUnauthorized());
  }

  @Test
  void itRejectsADuplicatedTitle() throws Exception {
    String token = authenticate();
    createCategory(token, "Office", "Office products");

    postJson(ENDPOINT, new CategoryRequest("  office ", "Other products"), token)
        .andExpect(status().isConflict())
        .andExpect(jsonPath("$.field").value("title"));
  }
}
