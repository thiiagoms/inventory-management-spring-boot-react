package io.thiiagoms.ims.product.domain;

import io.thiiagoms.ims.product.domain.valueobject.CategoryIds;
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

  private Title title;

  private Description description;

  private final Sku sku;

  private ImageUrl imageUrl;

  private Price price;

  private StockQuantity stockQuantity;

  private final CategoryIds categoryIds;

  private final ExpiryDate expiryDate;

  private Product(
      Id id,
      Title title,
      Description description,
      Sku sku,
      ImageUrl imageUrl,
      Price price,
      StockQuantity stockQuantity,
      CategoryIds categoryIds,
      ExpiryDate expiryDate) {
    Guard.againstNull(Id.FIELD, id);
    Guard.againstNull(Title.FIELD, title);
    Guard.againstNull(Description.FIELD, description);
    Guard.againstNull(Sku.FIELD, sku);
    Guard.againstNull(ImageUrl.FIELD, imageUrl);
    Guard.againstNull(Price.FIELD, price);
    Guard.againstNull(StockQuantity.FIELD, stockQuantity);
    Guard.againstNull(CategoryIds.FIELD, categoryIds);
    Guard.againstNull(ExpiryDate.FIELD, expiryDate);

    this.id = id;
    this.title = title;
    this.description = description;
    this.sku = sku;
    this.imageUrl = imageUrl;
    this.price = price;
    this.stockQuantity = stockQuantity;
    this.categoryIds = categoryIds;
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
      CategoryIds categoryIds,
      ExpiryDate expiryDate) {
    return new Product(
        id, title, description, sku, imageUrl, price, stockQuantity, categoryIds, expiryDate);
  }

  public static Product rehydrate(
      Id id,
      Title title,
      Description description,
      Sku sku,
      ImageUrl imageUrl,
      Price price,
      StockQuantity stockQuantity,
      CategoryIds categoryIds,
      ExpiryDate expiryDate) {
    return new Product(
        id, title, description, sku, imageUrl, price, stockQuantity, categoryIds, expiryDate);
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

  public CategoryIds categoryIds() {
    return categoryIds;
  }

  public ExpiryDate expiryDate() {
    return expiryDate;
  }

  public void changeTitleTo(Title title) {
    Guard.againstNull(Title.FIELD, title);
    if (this.title.equals(title)) {
      return;
    }
    this.title = title;
  }

  public void changeDescriptionTo(Description description) {
    Guard.againstNull(Description.FIELD, description);
    if (this.description.equals(description)) {
      return;
    }
    this.description = description;
  }

  public void changeImageUrlTo(ImageUrl imageUrl) {
    Guard.againstNull(ImageUrl.FIELD, imageUrl);
    if (this.imageUrl.equals(imageUrl)) {
      return;
    }
    this.imageUrl = imageUrl;
  }

  public void changePriceTo(Price price) {
    Guard.againstNull(Price.FIELD, price);
    if (this.price.equals(price)) {
      return;
    }
    this.price = price;
  }

  public void changeStockQuantityTo(StockQuantity stockQuantity) {
    Guard.againstNull(StockQuantity.FIELD, stockQuantity);
    if (this.stockQuantity.equals(stockQuantity)) {
      return;
    }
    this.stockQuantity = stockQuantity;
  }
}
