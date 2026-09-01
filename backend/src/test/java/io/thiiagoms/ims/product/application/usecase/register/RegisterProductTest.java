package io.thiiagoms.ims.product.application.usecase.register;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.thiiagoms.ims.category.application.exception.CategoryNotFoundException;
import io.thiiagoms.ims.category.application.service.CategoryFinder;
import io.thiiagoms.ims.fixtures.category.domain.CategoryFake;
import io.thiiagoms.ims.fixtures.category.infrastructure.persistence.repository.CategoryMemoryRepository;
import io.thiiagoms.ims.fixtures.product.domain.SkuGeneratorStub;
import io.thiiagoms.ims.fixtures.product.infrastructure.persistence.repository.ProductMemoryRepository;
import io.thiiagoms.ims.fixtures.shared.domain.identity.IdentityGeneratorStub;
import io.thiiagoms.ims.product.application.exception.ProductSkuAlreadyExistsException;
import io.thiiagoms.ims.product.application.exception.ProductTitleAlreadyExistsException;
import io.thiiagoms.ims.product.application.service.ProductUniqueness;
import io.thiiagoms.ims.product.domain.Product;
import io.thiiagoms.ims.product.domain.repository.ProductRepository;
import io.thiiagoms.ims.product.domain.valueobject.CategoryIds;
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
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RegisterProductTest {

  private static final CategoryIds CATEGORY_IDS =
      new CategoryIds(
          List.of(
              new Id("430e7bc1-59b9-472e-ae21-3cd90cde7caa"),
              new Id("3780baf2-deed-448d-a763-ce7b06efd394"),
              new Id("baa86496-638f-4beb-bc03-de2f7589ad63")));

  private static final Sku GENERATED_SKU =
      new Sku("office-chair-123e4567-e89b-12d3-a456-426614174000-1725134400000");

  private IdentityGeneratorStub identityGenerator;

  private SkuGeneratorStub skuGenerator;

  private ProductRepository repository;

  private CategoryMemoryRepository categoryRepository;

  private RegisterProduct useCase;

  private RegisterProductData data;

  @BeforeEach
  void setUp() {
    repository = new ProductMemoryRepository();
    categoryRepository = new CategoryMemoryRepository();
    categoryRepository.save(CategoryFake.start().withId(CATEGORY_IDS.values().get(0)).build());
    categoryRepository.save(
        CategoryFake.start()
            .withId(CATEGORY_IDS.values().get(1))
            .withTitle(new io.thiiagoms.ims.category.domain.valueobject.Title("Warehouse"))
            .build());
    categoryRepository.save(
        CategoryFake.start()
            .withId(CATEGORY_IDS.values().get(2))
            .withTitle(new io.thiiagoms.ims.category.domain.valueobject.Title("General"))
            .build());
    data =
        new RegisterProductData(
            new Title("Office Chair"),
            new Description("Ergonomic chair"),
            new ImageUrl("https://example.com/chair.png"),
            new Price(new BigDecimal("499.90")),
            new StockQuantity(10),
            CATEGORY_IDS,
            new ExpiryDate(LocalDateTime.of(2100, 1, 1, 0, 0)));
    identityGenerator = new IdentityGeneratorStub();
    skuGenerator = new SkuGeneratorStub().willGenerate(GENERATED_SKU);
    useCase = createUseCase();
  }

  @Test
  void itRegistersAProduct() {
    var id = new Id("0f18723a-fc95-493f-b223-bf78a81c1794");
    identityGenerator.willGenerate(id);

    var output = useCase.execute(data);

    assertEquals(id.value(), output.id());
    assertEquals("Office Chair", output.title());
    assertTrue(output.sku().matches("office-chair-[0-9a-f-]{36}-\\d{13}"));
    assertEquals(CATEGORY_IDS.values().stream().map(Id::value).toList(), output.categoryIds());
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
            data.categoryIds(),
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
                        data.categoryIds(),
                        data.expiryDate())));

    assertEquals(Title.FIELD, exception.getField());
    assertEquals("A product with this title already exists.", exception.getMessage());
    assertEquals(0, identityGenerator.numberOfCalls());
  }

  @Test
  void itRejectsADuplicateSku() {
    repository.save(
        Product.register(
            new Id("e312d572-b37c-4497-8b68-13b59db24ef5"),
            new Title("Desk"),
            data.description(),
            GENERATED_SKU,
            data.imageUrl(),
            data.price(),
            data.stockQuantity(),
            data.categoryIds(),
            data.expiryDate()));

    ProductSkuAlreadyExistsException exception =
        assertThrows(ProductSkuAlreadyExistsException.class, () -> useCase.execute(data));

    assertEquals(Sku.FIELD, exception.getField());
    assertEquals("A product with this SKU already exists.", exception.getMessage());
    assertEquals(0, identityGenerator.numberOfCalls());
  }

  @Test
  void itRejectsAnUnknownCategoryId() {
    var unknownCategoryId = new Id("de675b51-4817-4627-8873-d25b8462d2af");
    var categoryIds =
        new CategoryIds(
            List.of(CATEGORY_IDS.values().get(0), CATEGORY_IDS.values().get(1), unknownCategoryId));
    var invalidData =
        new RegisterProductData(
            data.title(),
            data.description(),
            data.imageUrl(),
            data.price(),
            data.stockQuantity(),
            categoryIds,
            data.expiryDate());

    CategoryNotFoundException exception =
        assertThrows(CategoryNotFoundException.class, () -> useCase.execute(invalidData));

    assertEquals(Id.FIELD, exception.getField());
    assertEquals("Category not found with the provided id.", exception.getMessage());
    assertEquals(0, identityGenerator.numberOfCalls());
    assertTrue(repository.findByTitle(data.title()).isEmpty());
  }

  private RegisterProduct createUseCase() {
    return new RegisterProduct(
        new CategoryFinder(categoryRepository),
        repository,
        identityGenerator,
        skuGenerator,
        new ProductUniqueness(repository));
  }
}
