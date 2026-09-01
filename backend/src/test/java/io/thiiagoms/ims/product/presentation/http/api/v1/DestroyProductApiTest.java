package io.thiiagoms.ims.product.presentation.http.api.v1;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.thiiagoms.ims.category.infrastructure.persistence.repository.CategoryJpaRepository;
import io.thiiagoms.ims.fixtures.product.presentation.http.api.v1.ProductApiTestSupport;
import io.thiiagoms.ims.product.infrastructure.persistence.repository.ProductJpaRepository;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;

public class DestroyProductApiTest extends ProductApiTestSupport {

  private final ProductJpaRepository repository;
  private final CategoryJpaRepository categoryRepository;
  private final JdbcTemplate jdbcTemplate;

  @Autowired
  DestroyProductApiTest(
      MockMvc mockMvc,
      ProductJpaRepository repository,
      CategoryJpaRepository categoryRepository,
      JdbcTemplate jdbcTemplate) {
    super(mockMvc);
    this.repository = repository;
    this.categoryRepository = categoryRepository;
    this.jdbcTemplate = jdbcTemplate;
  }

  @Test
  void itDestroysAProduct() throws Exception {
    String token = authenticate();
    var categoryIds = createProductCategories(token);
    String productId =
        postJsonAndReturnId(PRODUCT_ENDPOINT, productRequest("Office Chair", categoryIds), token);
    assertThat(countCategoryLinks(productId)).isEqualTo(categoryIds.size());

    deleteJson(PRODUCT_ENDPOINT + "/" + productId, token).andExpect(status().isNoContent());

    assertThat(repository.findById(UUID.fromString(productId))).isEmpty();
    assertThat(countCategoryLinks(productId)).isZero();
    assertThat(categoryIds)
        .allSatisfy(
            categoryId ->
                assertThat(categoryRepository.findById(UUID.fromString(categoryId))).isPresent());
  }

  @Test
  void itReturnsNotFoundForAnUnknownProduct() throws Exception {
    String token = authenticate();

    deleteJson(PRODUCT_ENDPOINT + "/3780baf2-deed-448d-a763-ce7b06efd394", token)
        .andExpect(status().isNotFound());
  }

  private int countCategoryLinks(String productId) {
    Integer count =
        jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM product_categories WHERE product_id = UUID_TO_BIN(?)",
            Integer.class,
            productId);
    return count == null ? 0 : count;
  }
}
