package io.thiiagoms.ims.fixtures.product.domain;

import io.thiiagoms.ims.product.domain.Product;
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

public class ProductFake {
  private Id id;
  private Title title;
  private Description description;
  private Sku sku;
  private ImageUrl imageUrl;
  private Price price;
  private StockQuantity stockQuantity;
  private Id categoryId;
  private ExpiryDate expiryDate;

  private ProductFake() {
    id = new Id("0f18723a-fc95-493f-b223-bf78a81c1794");
    title = new Title("Office Chair");
    description = new Description("Ergonomic office chair");
    sku = new Sku("CHAIR-001");
    imageUrl = new ImageUrl("https://example.com/chair.png");
    price = new Price(new BigDecimal("499.90"));
    stockQuantity = new StockQuantity(10);
    categoryId = new Id("430e7bc1-59b9-472e-ae21-3cd90cde7caa");
    expiryDate = new ExpiryDate(LocalDateTime.of(2100, 1, 1, 0, 0));
  }

  public static ProductFake start() {
    return new ProductFake();
  }

  public ProductFake withId(Id id) {
    this.id = id;
    return this;
  }

  public ProductFake withTitle(Title title) {
    this.title = title;
    return this;
  }

  public ProductFake withSku(Sku sku) {
    this.sku = sku;
    return this;
  }

  public Product build() {
    return Product.rehydrate(
        id, title, description, sku, imageUrl, price, stockQuantity, categoryId, expiryDate);
  }
}
