package io.thiiagoms.ims.product.domain;

import io.thiiagoms.ims.product.domain.valueobject.Description;
import io.thiiagoms.ims.product.domain.valueobject.ExpiryDate;
import io.thiiagoms.ims.product.domain.valueobject.ImageUrl;
import io.thiiagoms.ims.product.domain.valueobject.Price;
import io.thiiagoms.ims.product.domain.valueobject.Sku;
import io.thiiagoms.ims.product.domain.valueobject.StockQuantity;
import io.thiiagoms.ims.product.domain.valueobject.Title;
import io.thiiagoms.ims.shared.domain.support.Guard;
import io.thiiagoms.ims.shared.domain.valueobject.Id;

public final class Product {

  private final Id id;

  private final Title title;

  private final Description description;

  private final Sku sku;

  private final ImageUrl imageUrl;

  private final Price price;

  private final StockQuantity stockQuantity;

  private final Id categoryId;

  private final ExpiryDate expiryDate;

  private Product(
      Id id,
      Title title,
      Description description,
      Sku sku,
      ImageUrl imageUrl,
      Price price,
      StockQuantity stockQuantity,
      Id categoryId,
      ExpiryDate expiryDate) {
    Guard.againstNull(Id.FIELD, id);
    Guard.againstNull(Title.FIELD, title);
    Guard.againstNull(Description.FIELD, description);
    Guard.againstNull(Sku.FIELD, sku);
    Guard.againstNull(ImageUrl.FIELD, imageUrl);
    Guard.againstNull(Price.FIELD, price);
    Guard.againstNull(StockQuantity.FIELD, stockQuantity);
    Guard.againstNull("categoryId", categoryId);
    Guard.againstNull(ExpiryDate.FIELD, expiryDate);

    this.id = id;
    this.title = title;
    this.description = description;
    this.sku = sku;
    this.imageUrl = imageUrl;
    this.price = price;
    this.stockQuantity = stockQuantity;
    this.categoryId = categoryId;
    this.expiryDate = expiryDate;
  }

  public static Product register(
      Id id,
      Title title,
      Description description,
      Sku sku,
      ImageUrl imageUrl,
      Price price,
      StockQuantity stockQuantity,
      Id categoryId,
      ExpiryDate expiryDate) {
    return new Product(
        id, title, description, sku, imageUrl, price, stockQuantity, categoryId, expiryDate);
  }

  public static Product rehydrate(
      Id id,
      Title title,
      Description description,
      Sku sku,
      ImageUrl imageUrl,
      Price price,
      StockQuantity stockQuantity,
      Id categoryId,
      ExpiryDate expiryDate) {
    return new Product(
        id, title, description, sku, imageUrl, price, stockQuantity, categoryId, expiryDate);
  }

  public Id id() {
    return id;
  }

  public Title title() {
    return title;
  }

  public Description description() {
    return description;
  }

  public Sku sku() {
    return sku;
  }

  public ImageUrl imageUrl() {
    return imageUrl;
  }

  public Price price() {
    return price;
  }

  public StockQuantity stockQuantity() {
    return stockQuantity;
  }

  public Id categoryId() {
    return categoryId;
  }

  public ExpiryDate expiryDate() {
    return expiryDate;
  }
}
