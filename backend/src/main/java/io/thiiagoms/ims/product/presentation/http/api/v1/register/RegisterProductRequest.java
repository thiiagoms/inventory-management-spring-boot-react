package io.thiiagoms.ims.product.presentation.http.api.v1.register;

import io.thiiagoms.ims.product.application.usecase.register.RegisterProductData;
import io.thiiagoms.ims.product.domain.valueobject.CategoryIds;
import io.thiiagoms.ims.product.domain.valueobject.Description;
import io.thiiagoms.ims.product.domain.valueobject.ExpiryDate;
import io.thiiagoms.ims.product.domain.valueobject.ImageUrl;
import io.thiiagoms.ims.product.domain.valueobject.Price;
import io.thiiagoms.ims.product.domain.valueobject.StockQuantity;
import io.thiiagoms.ims.product.domain.valueobject.Title;
import io.thiiagoms.ims.shared.domain.valueobject.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record RegisterProductRequest(
    @NotBlank String title,
    @NotBlank String description,
    @NotBlank String imageUrl,
    @NotNull @Positive BigDecimal price,
    @NotNull @Positive Integer stockQuantity,
    @NotNull List<@NotBlank String> categoryIds,
    @NotNull LocalDateTime expiryDate) {
  public RegisterProductRequest {
    categoryIds = categoryIds == null ? null : List.copyOf(categoryIds);
  }

  public RegisterProductData toCommand() {
    return new RegisterProductData(
        new Title(title),
        new Description(description),
        new ImageUrl(imageUrl),
        new Price(price),
        new StockQuantity(stockQuantity),
        new CategoryIds(categoryIds.stream().map(Id::new).toList()),
        new ExpiryDate(expiryDate));
  }
}
