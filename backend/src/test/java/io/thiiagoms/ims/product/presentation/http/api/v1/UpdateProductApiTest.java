package io.thiiagoms.ims.product.presentation.http.api.v1;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.thiiagoms.ims.fixtures.product.presentation.http.api.v1.ProductApiTestSupport;
import io.thiiagoms.ims.product.presentation.http.api.v1.update.UpdateProductRequest;
import java.math.BigDecimal;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

class UpdateProductApiTest extends ProductApiTestSupport {
  @Autowired
  UpdateProductApiTest(MockMvc mockMvc) {
    super(mockMvc);
  }

  @Test
  void itUpdatesAProduct() throws Exception {
    String token = authenticate();
    String productId = createProduct(token);
    var request =
        new UpdateProductRequest(
            "meeting chair",
            "Adjustable meeting room chair",
            "https://example.com/meeting-chair.png",
            new BigDecimal("699.90"),
            20);

    patchJson(PRODUCT_ENDPOINT + "/" + productId, request, token)
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.title").value("Meeting Chair"))
        .andExpect(jsonPath("$.sku").value(org.hamcrest.Matchers.startsWith("office-chair-")))
        .andExpect(jsonPath("$.description").value(request.description()))
        .andExpect(jsonPath("$.imageUrl").value(request.imageUrl()))
        .andExpect(jsonPath("$.price").value(699.90))
        .andExpect(jsonPath("$.stockQuantity").value(20));
  }

  @Test
  void itReturnsNotFoundWhenTheProductDoesNotExist() throws Exception {
    String token = authenticate();
    var request = new UpdateProductRequest("Meeting Chair", null, null, null, null);

    patchJson(PRODUCT_ENDPOINT + "/3780baf2-deed-448d-a763-ce7b06efd394", request, token)
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.status").value(404))
        .andExpect(jsonPath("$.error").value("resource_not_found"))
        .andExpect(jsonPath("$.field").value("id"))
        .andExpect(jsonPath("$.message").value("Product not found with the provided id."));
  }

  @Test
  void itUpdatesOnlyTitle() throws Exception {
    String token = authenticate();
    String productId = createProduct(token);
    var request = new UpdateProductRequest("meeting chair", null, null, null, null);

    patchJson(PRODUCT_ENDPOINT + "/" + productId, request, token)
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.title").value("Meeting Chair"))
        .andExpect(jsonPath("$.description").value("Ergonomic office chair"))
        .andExpect(jsonPath("$.imageUrl").value("https://example.com/chair.png"))
        .andExpect(jsonPath("$.price").value(499.90))
        .andExpect(jsonPath("$.stockQuantity").value(10));
  }

  @Test
  void itUpdatesOnlyDescription() throws Exception {
    String token = authenticate();
    String productId = createProduct(token);
    var request = new UpdateProductRequest(null, "Adjustable chair", null, null, null);

    patchJson(PRODUCT_ENDPOINT + "/" + productId, request, token)
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.title").value("Office Chair"))
        .andExpect(jsonPath("$.description").value(request.description()))
        .andExpect(jsonPath("$.imageUrl").value("https://example.com/chair.png"))
        .andExpect(jsonPath("$.price").value(499.90))
        .andExpect(jsonPath("$.stockQuantity").value(10));
  }

  @Test
  void itUpdatesOnlyImageUrl() throws Exception {
    String token = authenticate();
    String productId = createProduct(token);
    var request =
        new UpdateProductRequest(null, null, "https://example.com/meeting-chair.png", null, null);

    patchJson(PRODUCT_ENDPOINT + "/" + productId, request, token)
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.title").value("Office Chair"))
        .andExpect(jsonPath("$.description").value("Ergonomic office chair"))
        .andExpect(jsonPath("$.imageUrl").value(request.imageUrl()))
        .andExpect(jsonPath("$.price").value(499.90))
        .andExpect(jsonPath("$.stockQuantity").value(10));
  }

  @Test
  void itUpdatesOnlyPrice() throws Exception {
    String token = authenticate();
    String productId = createProduct(token);
    var request = new UpdateProductRequest(null, null, null, new BigDecimal("599.90"), null);

    patchJson(PRODUCT_ENDPOINT + "/" + productId, request, token)
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.title").value("Office Chair"))
        .andExpect(jsonPath("$.description").value("Ergonomic office chair"))
        .andExpect(jsonPath("$.imageUrl").value("https://example.com/chair.png"))
        .andExpect(jsonPath("$.price").value(599.90))
        .andExpect(jsonPath("$.stockQuantity").value(10));
  }

  @Test
  void itIncreasesOnlyStockQuantity() throws Exception {
    String token = authenticate();
    String productId = createProduct(token);
    var request = new UpdateProductRequest(null, null, null, null, 20);

    patchJson(PRODUCT_ENDPOINT + "/" + productId, request, token)
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.price").value(499.90))
        .andExpect(jsonPath("$.stockQuantity").value(20));
  }

  @Test
  void itDecreasesOnlyStockQuantity() throws Exception {
    String token = authenticate();
    String productId = createProduct(token);
    var request = new UpdateProductRequest(null, null, null, null, 5);

    patchJson(PRODUCT_ENDPOINT + "/" + productId, request, token)
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.price").value(499.90))
        .andExpect(jsonPath("$.stockQuantity").value(5));
  }

  @ParameterizedTest(name = "[{index}] stock quantity {0}")
  @MethodSource("invalidStockQuantityProvider")
  void itValidatesInvalidStockQuantity(Integer invalidStockQuantity) throws Exception {
    assertValidationError(
        new UpdateProductRequest(null, null, null, null, invalidStockQuantity),
        "stockQuantity",
        "Stock quantity must be greater than zero.");
  }

  @ParameterizedTest(name = "[{index}] {0}")
  @MethodSource("invalidTitleProvider")
  void itValidatesInvalidTitle(String scenario, String invalidTitle, String expectedMessage)
      throws Exception {
    assertValidationError(
        new UpdateProductRequest(invalidTitle, null, null, null, null), "title", expectedMessage);
  }

  @ParameterizedTest(name = "[{index}] {0}")
  @MethodSource("invalidDescriptionProvider")
  void itValidatesInvalidDescription(
      String scenario, String invalidDescription, String expectedMessage) throws Exception {
    assertValidationError(
        new UpdateProductRequest(null, invalidDescription, null, null, null),
        "description",
        expectedMessage);
  }

  @ParameterizedTest(name = "[{index}] {0}")
  @MethodSource("invalidImageUrlProvider")
  void itValidatesInvalidImageUrl(String scenario, String invalidImageUrl, String expectedMessage)
      throws Exception {
    assertValidationError(
        new UpdateProductRequest(null, null, invalidImageUrl, null, null),
        "imageUrl",
        expectedMessage);
  }

  @ParameterizedTest(name = "[{index}] {0}")
  @MethodSource("invalidPriceProvider")
  void itValidatesInvalidPrice(String scenario, BigDecimal invalidPrice, String expectedMessage)
      throws Exception {
    assertValidationError(
        new UpdateProductRequest(null, null, null, invalidPrice, null), "price", expectedMessage);
  }

  @Test
  void itRejectsATitleOwnedByAnotherProduct() throws Exception {
    String token = authenticate();
    var categoryIds = createProductCategories(token);
    postJsonAndReturnId(PRODUCT_ENDPOINT, productRequest("Office Chair", categoryIds), token);
    String productId =
        postJsonAndReturnId(PRODUCT_ENDPOINT, productRequest("Meeting Chair", categoryIds), token);

    patchJson(
            PRODUCT_ENDPOINT + "/" + productId,
            new UpdateProductRequest("office chair", null, null, null, null),
            token)
        .andExpect(status().isConflict())
        .andExpect(jsonPath("$.field").value("title"));
  }

  @Test
  void itRejectsAnUpdateWithoutChanges() throws Exception {
    String token = authenticate();
    String productId = createProduct(token);

    patchJson(
            PRODUCT_ENDPOINT + "/" + productId,
            new UpdateProductRequest(null, null, null, null, null),
            token)
        .andExpect(status().isUnprocessableContent())
        .andExpect(jsonPath("$.error").value("resource_not_changed"));
  }

  private String createProduct(String token) throws Exception {
    var categoryIds = createProductCategories(token);
    return postJsonAndReturnId(
        PRODUCT_ENDPOINT, productRequest("Office Chair", categoryIds), token);
  }

  private void assertValidationError(
      UpdateProductRequest request, String expectedField, String expectedMessage) throws Exception {
    String token = authenticate();
    String productId = createProduct(token);

    patchJson(PRODUCT_ENDPOINT + "/" + productId, request, token)
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status").value(400))
        .andExpect(jsonPath("$.error").value("validation_failed"))
        .andExpect(jsonPath("$.field").value(expectedField))
        .andExpect(jsonPath("$.message").value(expectedMessage));
  }

  private static Stream<Arguments> invalidTitleProvider() {
    return Stream.of(
        Arguments.of("blank", "", "The field 'title' cannot be null, empty or blank."),
        Arguments.of(
            "below minimum length", "ab", "Title value must be between 3 and 250 characters long."),
        Arguments.of(
            "invalid characters",
            "Chair 123",
            "Title must contain only letters, spaces, apostrophes, dots, and hyphens."));
  }

  private static Stream<Arguments> invalidDescriptionProvider() {
    return Stream.of(
        Arguments.of("blank", "", "The field 'description' cannot be null, empty or blank."),
        Arguments.of(
            "below minimum length", "ab", "Description must be between 3 and 255 characters long."),
        Arguments.of(
            "above maximum length",
            "a".repeat(256),
            "Description must be between 3 and 255 characters long."));
  }

  private static Stream<Arguments> invalidImageUrlProvider() {
    return Stream.of(
        Arguments.of("blank", "", "The field 'imageUrl' cannot be null, empty or blank."),
        Arguments.of(
            "relative URL", "/chair.png", "Image URL must be an absolute HTTP or HTTPS URL."),
        Arguments.of(
            "unsupported scheme",
            "ftp://example.com/chair.png",
            "Image URL must be an absolute HTTP or HTTPS URL."));
  }

  private static Stream<Arguments> invalidPriceProvider() {
    return Stream.of(
        Arguments.of("zero", BigDecimal.ZERO, "Price must be greater than zero."),
        Arguments.of("negative", new BigDecimal("-1.00"), "Price must be greater than zero."),
        Arguments.of(
            "more than two decimal places",
            new BigDecimal("10.001"),
            "Price must have at most two decimal places."));
  }

  private static Stream<Integer> invalidStockQuantityProvider() {
    return Stream.of(0, -1);
  }
}
