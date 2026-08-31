package io.thiiagoms.ims.product.application.usecase.update;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.thiiagoms.ims.fixtures.product.domain.ProductFake;
import io.thiiagoms.ims.fixtures.product.infrastructure.persistence.repository.ProductMemoryRepository;
import io.thiiagoms.ims.product.application.exception.ProductNotChangedException;
import io.thiiagoms.ims.product.application.exception.ProductNotFoundException;
import io.thiiagoms.ims.product.application.exception.ProductTitleAlreadyExistsException;
import io.thiiagoms.ims.product.application.service.ProductFinder;
import io.thiiagoms.ims.product.application.service.ProductUniqueness;
import io.thiiagoms.ims.product.domain.Product;
import io.thiiagoms.ims.product.domain.repository.ProductRepository;
import io.thiiagoms.ims.product.domain.valueobject.Description;
import io.thiiagoms.ims.product.domain.valueobject.ImageUrl;
import io.thiiagoms.ims.product.domain.valueobject.Price;
import io.thiiagoms.ims.product.domain.valueobject.Sku;
import io.thiiagoms.ims.product.domain.valueobject.StockQuantity;
import io.thiiagoms.ims.product.domain.valueobject.Title;
import io.thiiagoms.ims.shared.domain.valueobject.Id;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UpdateProductTest {

  private Id id;

  private ProductRepository repository;

  private UpdateProduct useCase;

  private Product product;

  @BeforeEach
  void setUp() {
    id = new Id("0f18723a-fc95-493f-b223-bf78a81c1794");
    repository = new ProductMemoryRepository();
    useCase =
        new UpdateProduct(
            new ProductFinder(repository), repository, new ProductUniqueness(repository));
    product = ProductFake.start().withId(id).build();
    repository.save(product);
  }

  @Test
  void itUpdatesEveryMutableProductField() {
    var title = new Title("Meeting Chair");
    var description = new Description("Adjustable meeting room chair");
    var imageUrl = new ImageUrl("https://example.com/meeting-chair.png");
    var price = new Price(new BigDecimal("699.90"));
    var stockQuantity = new StockQuantity(20);
    var categoryId = product.categoryId();
    var expiryDate = product.expiryDate();
    var data =
        new UpdateProductData(
            id,
            Optional.of(title),
            Optional.of(description),
            Optional.of(imageUrl),
            Optional.of(price),
            Optional.of(stockQuantity));

    var output = useCase.execute(data);

    assertEquals(title.value(), output.title());
    assertEquals(description.value(), output.description());
    assertEquals(product.sku().value(), output.sku());
    assertEquals(imageUrl.value(), output.imageUrl());
    assertEquals(price.value(), output.price());
    assertEquals(stockQuantity.value(), output.stockQuantity());
    assertEquals(categoryId.value(), output.categoryId());
    assertEquals(expiryDate.value(), output.expiryDate());
  }

  @Test
  void itUpdatesOnlyTitle() {
    var title = new Title("Meeting Chair");
    var description = product.description();

    var output =
        useCase.execute(
            data(
                Optional.of(title),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty()));

    assertEquals(title.value(), output.title());
    assertEquals(description.value(), output.description());
  }

  @Test
  void itUpdatesOnlyDescription() {
    var title = product.title();
    var description = new Description("Adjustable meeting room chair");

    var output =
        useCase.execute(
            data(
                Optional.empty(),
                Optional.of(description),
                Optional.empty(),
                Optional.empty(),
                Optional.empty()));

    assertEquals(title.value(), output.title());
    assertEquals(description.value(), output.description());
  }

  @Test
  void itUpdatesOnlyImageUrl() {
    var imageUrl = new ImageUrl("https://example.com/meeting-chair.png");
    var price = product.price();

    var output =
        useCase.execute(
            data(
                Optional.empty(),
                Optional.empty(),
                Optional.of(imageUrl),
                Optional.empty(),
                Optional.empty()));

    assertEquals(imageUrl.value(), output.imageUrl());
    assertEquals(price.value(), output.price());
  }

  @Test
  void itUpdatesOnlyPrice() {
    var price = new Price(new BigDecimal("599.90"));
    var stockQuantity = product.stockQuantity();

    var output =
        useCase.execute(
            data(
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.of(price),
                Optional.empty()));

    assertEquals(price.value(), output.price());
    assertEquals(stockQuantity.value(), output.stockQuantity());
  }

  @Test
  void itUpdatesOnlyStockQuantity() {
    var price = product.price();
    var stockQuantity = new StockQuantity(20);

    var output =
        useCase.execute(
            data(
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.of(stockQuantity)));

    assertEquals(price.value(), output.price());
    assertEquals(stockQuantity.value(), output.stockQuantity());
  }

  @Test
  void itRejectsAnUnknownProductId() {
    var unknownId = new Id("baa86496-638f-4beb-bc03-de2f7589ad63");

    ProductNotFoundException exception =
        assertThrows(ProductNotFoundException.class, () -> useCase.execute(emptyData(unknownId)));

    assertEquals(Id.FIELD, exception.getField());
    assertEquals("Product not found with the provided id.", exception.getMessage());
  }

  @Test
  void itRejectsATitleAlreadyOwnedByAnotherProduct() {
    var duplicateTitle = new Title("Warehouse Chair");
    repository.save(
        ProductFake.start()
            .withId(new Id("baa86496-638f-4beb-bc03-de2f7589ad63"))
            .withTitle(duplicateTitle)
            .withSku(new Sku("CHAIR-002"))
            .build());
    var data =
        data(
            Optional.of(duplicateTitle),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty());

    ProductTitleAlreadyExistsException exception =
        assertThrows(ProductTitleAlreadyExistsException.class, () -> useCase.execute(data));

    assertEquals(Title.FIELD, exception.getField());
  }

  @Test
  void itRejectsAnUpdateWithoutChanges() {
    var data =
        data(
            Optional.of(product.title()),
            Optional.of(product.description()),
            Optional.of(product.imageUrl()),
            Optional.of(product.price()),
            Optional.of(product.stockQuantity()));

    ProductNotChangedException exception =
        assertThrows(ProductNotChangedException.class, () -> useCase.execute(data));

    assertEquals(Id.FIELD, exception.getField());
    assertEquals("The product was not changed.", exception.getMessage());
  }

  private UpdateProductData emptyData(Id productId) {
    return new UpdateProductData(
        productId,
        Optional.empty(),
        Optional.empty(),
        Optional.empty(),
        Optional.empty(),
        Optional.empty());
  }

  private UpdateProductData data(
      Optional<Title> title,
      Optional<Description> description,
      Optional<ImageUrl> imageUrl,
      Optional<Price> price,
      Optional<StockQuantity> stockQuantity) {
    return new UpdateProductData(id, title, description, imageUrl, price, stockQuantity);
  }
}
