package io.thiiagoms.ims.product.application.usecase.retrieve;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.thiiagoms.ims.fixtures.product.domain.ProductFake;
import io.thiiagoms.ims.fixtures.product.infrastructure.persistence.repository.ProductMemoryRepository;
import io.thiiagoms.ims.product.domain.valueobject.Sku;
import io.thiiagoms.ims.product.domain.valueobject.Title;
import io.thiiagoms.ims.shared.domain.pagination.Pagination;
import io.thiiagoms.ims.shared.domain.valueobject.Id;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RetrieveProductsTest {

  private ProductMemoryRepository repository;

  private RetrieveProducts useCase;

  @BeforeEach
  void setUp() {
    repository = new ProductMemoryRepository();
    useCase = new RetrieveProducts(repository);
  }

  @Test
  void itRetrievesTheFirstProductPage() {
    saveProducts();

    var output = useCase.execute(new Pagination(0, 2));

    assertEquals(2, output.content().size());
    assertEquals("Meeting Chair", output.content().get(0).title());
    assertEquals("Office Chair", output.content().get(1).title());
    assertEquals(0, output.page());
    assertEquals(2, output.size());
    assertEquals(3, output.totalElements());
    assertEquals(2, output.totalPages());
    assertTrue(output.first());
    assertFalse(output.last());
  }

  @Test
  void itRetrievesTheLastProductPage() {
    saveProducts();

    var output = useCase.execute(new Pagination(1, 2));

    assertEquals(1, output.content().size());
    assertEquals("Warehouse Chair", output.content().getFirst().title());
    assertEquals(1, output.page());
    assertFalse(output.first());
    assertTrue(output.last());
  }

  @Test
  void itRetrievesAnEmptyProductPage() {
    var output = useCase.execute(new Pagination(0, 20));

    assertTrue(output.content().isEmpty());
    assertEquals(0, output.totalElements());
    assertEquals(0, output.totalPages());
    assertTrue(output.first());
    assertTrue(output.last());
  }

  private void saveProducts() {
    repository.save(ProductFake.start().build());
    repository.save(
        ProductFake.start()
            .withId(new Id("baa86496-638f-4beb-bc03-de2f7589ad63"))
            .withTitle(new Title("Warehouse Chair"))
            .withSku(new Sku("warehouse-chair"))
            .build());
    repository.save(
        ProductFake.start()
            .withId(new Id("3780baf2-deed-448d-a763-ce7b06efd394"))
            .withTitle(new Title("Meeting Chair"))
            .withSku(new Sku("meeting-chair"))
            .build());
  }
}
