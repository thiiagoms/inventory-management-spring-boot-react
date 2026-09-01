package io.thiiagoms.ims.product.application.usecase.register;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.thiiagoms.ims.fixtures.product.infrastructure.persistence.repository.ProductMemoryRepository;
import io.thiiagoms.ims.fixtures.shared.domain.identity.IdentityGeneratorStub;
import io.thiiagoms.ims.product.application.exception.ProductTitleAlreadyExistsException;
import io.thiiagoms.ims.product.application.service.ProductUniqueness;
import io.thiiagoms.ims.product.domain.Product;
import io.thiiagoms.ims.product.domain.repository.ProductRepository;
import io.thiiagoms.ims.product.domain.valueobject.Description;
import io.thiiagoms.ims.product.domain.valueobject.ExpiryDate;
import io.thiiagoms.ims.product.domain.valueobject.ImageUrl;
import io.thiiagoms.ims.product.domain.valueobject.Price;
import io.thiiagoms.ims.product.domain.valueobject.Sku;
import io.thiiagoms.ims.product.domain.valueobject.StockQuantity;
import io.thiiagoms.ims.product.domain.valueobject.Title;
import io.thiiagoms.ims.shared.domain.valueobject.Id;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RegisterProductTest {

  private static final Id CATEGORY_ID = new Id("430e7bc1-59b9-472e-ae21-3cd90cde7caa");

  private IdentityGeneratorStub identityGenerator;

  private ProductRepository repository;

  private RegisterProduct useCase;

  private RegisterProductData data;

  @BeforeEach
  void setUp() {
    repository = new ProductMemoryRepository();
    identityGenerator = new IdentityGeneratorStub();
    useCase = new RegisterProduct(repository, identityGenerator, new ProductUniqueness(repository));
    data =
        new RegisterProductData(
            new Title("Office Chair"),
            new Description("Ergonomic chair"),
            new ImageUrl("https://example.com/chair.png"),
            new Price(new BigDecimal("499.90")),
            new StockQuantity(10),
            CATEGORY_ID,
            new ExpiryDate(LocalDateTime.of(2100, 1, 1, 0, 0)));
  }

  @Test
  void itRegistersAProduct() {
    var id = new Id("0f18723a-fc95-493f-b223-bf78a81c1794");
    identityGenerator.willGenerate(id);

    var output = useCase.execute(data);

    assertEquals(id.value(), output.id());
    assertEquals("Office Chair", output.title());
    assertTrue(output.sku().matches("office-chair-[0-9a-f-]{36}-\\d{13}"));
    assertEquals(CATEGORY_ID.value(), output.categoryId());
    assertEquals(data.price().value(), output.price());
  }

  @Test
  void itRejectsANormalizedDuplicateTitle() {
    repository.save(
        Product.register(
            new Id("e312d572-b37c-4497-8b68-13b59db24ef5"),
            data.title(),
            data.description(),
            new Sku("office-chair-existing"),
            data.imageUrl(),
            data.price(),
            data.stockQuantity(),
            data.categoryId(),
            data.expiryDate()));

    ProductTitleAlreadyExistsException exception =
        assertThrows(
            ProductTitleAlreadyExistsException.class,
            () ->
                useCase.execute(
                    new RegisterProductData(
                        new Title("  office chair "),
                        data.description(),
                        data.imageUrl(),
                        data.price(),
                        data.stockQuantity(),
                        data.categoryId(),
                        data.expiryDate())));

    assertEquals(Title.FIELD, exception.getField());
    assertEquals("A product with this title already exists.", exception.getMessage());
    assertEquals(0, identityGenerator.numberOfCalls());
  }
}
