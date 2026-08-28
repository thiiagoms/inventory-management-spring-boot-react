package io.thiiagoms.ims.category.presentation.http.api.v1;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.thiiagoms.ims.category.infrastructure.persistence.repository.CategoryJpaRepository;
import io.thiiagoms.ims.fixtures.category.presentation.http.api.v1.CategoryApiTestSupport;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

class DestroyCategoryApiTest extends CategoryApiTestSupport {
  private final CategoryJpaRepository repository;

  @Autowired
  DestroyCategoryApiTest(MockMvc mockMvc, CategoryJpaRepository repository) {
    super(mockMvc);
    this.repository = repository;
  }

  @Test
  void itDestroysACategory() throws Exception {
    String token = authenticate();
    String id = createCategory(token, "Office", "Office products");

    deleteJson(ENDPOINT + "/" + id, token).andExpect(status().isNoContent());

    assertThat(repository.findById(UUID.fromString(id))).isEmpty();
  }

  @Test
  void itReturnsNotFoundForAnUnknownCategory() throws Exception {
    String token = authenticate();

    deleteJson(ENDPOINT + "/3780baf2-deed-448d-a763-ce7b06efd394", token)
        .andExpect(status().isNotFound());
  }
}
