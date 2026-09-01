package io.thiiagoms.ims.product.application.usecase.retrieve;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.thiiagoms.ims.fixtures.product.domain.ProductFake;
import io.thiiagoms.ims.fixtures.product.infrastructure.persistence.repository.ProductMemoryRepository;
import io.thiiagoms.ims.product.application.exception.ProductNotFoundException;
import io.thiiagoms.ims.product.application.service.ProductFinder;
import io.thiiagoms.ims.shared.domain.valueobject.Id;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RetrieveProductTest {

  private ProductMemoryRepository repository;

  private RetrieveProduct useCase;

  @BeforeEach
  void setUp() {
    repository = new ProductMemoryRepository();
    useCase = new RetrieveProduct(new ProductFinder(repository));
  }

  @Test
  void itRetrievesAProductById() {
    var product = ProductFake.start().build();
    repository.save(product);

    var output = useCase.execute(product.id());

    assertEquals(product.id().value(), output.id());
    assertEquals(product.title().value(), output.title());
    assertEquals(product.sku().value(), output.sku());
    assertEquals(
        product.categoryIds().values().stream().map(Id::value).toList(), output.categoryIds());
  }

  @Test
  void itRejectsAnUnknownProductId() {
    var unknownId = new Id("3780baf2-deed-448d-a763-ce7b06efd394");

    ProductNotFoundException exception =
        assertThrows(ProductNotFoundException.class, () -> useCase.execute(unknownId));

    assertEquals(Id.FIELD, exception.getField());
    assertEquals("Product not found with the provided id.", exception.getMessage());
  }
}
