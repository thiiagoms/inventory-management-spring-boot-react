package io.thiiagoms.ims.product.presentation.http.api.v1.update;

import io.thiiagoms.ims.product.application.usecase.update.UpdateProductData;
import io.thiiagoms.ims.product.domain.valueobject.Description;
import io.thiiagoms.ims.product.domain.valueobject.ImageUrl;
import io.thiiagoms.ims.product.domain.valueobject.Price;
import io.thiiagoms.ims.product.domain.valueobject.StockQuantity;
import io.thiiagoms.ims.product.domain.valueobject.Title;
import io.thiiagoms.ims.shared.domain.valueobject.Id;
import java.math.BigDecimal;
import java.util.Optional;

public record UpdateProductRequest(
    String title, String description, String imageUrl, BigDecimal price, Integer stockQuantity) {

  public UpdateProductData toCommand(String id) {
    return new UpdateProductData(
        new Id(id),
        Optional.ofNullable(title).map(Title::new),
        Optional.ofNullable(description).map(Description::new),
        Optional.ofNullable(imageUrl).map(ImageUrl::new),
        Optional.ofNullable(price).map(Price::new),
        Optional.ofNullable(stockQuantity).map(StockQuantity::new));
  }
}
