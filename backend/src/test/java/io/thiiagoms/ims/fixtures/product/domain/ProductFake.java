package io.thiiagoms.ims.fixtures.product.domain;

import io.thiiagoms.ims.product.domain.Product;
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

public class ProductFake {
  private Id id;
  private Title title;
  private Description description;
  private Sku sku;
  private ImageUrl imageUrl;
  private Price price;
  private StockQuantity stockQuantity;
  private CategoryIds categoryIds;
  private ExpiryDate expiryDate;

  private ProductFake() {
    id = new Id("0f18723a-fc95-493f-b223-bf78a81c1794");
    title = new Title("Office Chair");
    description = new Description("Ergonomic office chair");
    sku = new Sku("CHAIR-001");
    imageUrl = new ImageUrl("https://example.com/chair.png");
    price = new Price(new BigDecimal("499.90"));
    stockQuantity = new StockQuantity(10);
    categoryIds =
        new CategoryIds(
            List.of(
                new Id("430e7bc1-59b9-472e-ae21-3cd90cde7caa"),
                new Id("3780baf2-deed-448d-a763-ce7b06efd394"),
                new Id("baa86496-638f-4beb-bc03-de2f7589ad63")));
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

  public ProductFake withCategoryIds(CategoryIds categoryIds) {
    this.categoryIds = categoryIds;
    return this;
  }

  public Product build() {
    return Product.rehydrate(
        id, title, description, sku, imageUrl, price, stockQuantity, categoryIds, expiryDate);
  }
}
