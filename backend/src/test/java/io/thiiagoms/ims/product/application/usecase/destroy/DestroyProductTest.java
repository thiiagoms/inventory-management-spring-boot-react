package io.thiiagoms.ims.product.application.usecase.destroy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.thiiagoms.ims.fixtures.product.domain.ProductFake;
import io.thiiagoms.ims.fixtures.product.infrastructure.persistence.repository.ProductMemoryRepository;
import io.thiiagoms.ims.product.application.exception.ProductNotFoundException;
import io.thiiagoms.ims.product.application.service.ProductFinder;
import io.thiiagoms.ims.product.domain.repository.ProductRepository;
import io.thiiagoms.ims.shared.domain.valueobject.Id;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DestroyProductTest {

  private Id id;

  private ProductRepository repository;

  private DestroyProduct useCase;

  @BeforeEach
  void setUp() {
    id = new Id("3780baf2-deed-448d-a763-ce7b06efd394");
    repository = new ProductMemoryRepository();
    useCase = new DestroyProduct(new ProductFinder(repository), repository);
  }

  @Test
  void itDestroysAnExistingProduct() {
    repository.save(ProductFake.start().withId(id).build());

    useCase.execute(id);

    assertTrue(repository.findById(id).isEmpty());
  }

  @Test
  void itRejectsDestroyingAnUnknownProduct() {
    ProductNotFoundException exception =
        assertThrows(ProductNotFoundException.class, () -> useCase.execute(id));

    assertEquals(Id.FIELD, exception.getField());
    assertEquals("Product not found with the provided id.", exception.getMessage());
  }
}
