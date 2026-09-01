package io.thiiagoms.ims.product.application.usecase.update;

import io.thiiagoms.ims.product.domain.valueobject.Description;
import io.thiiagoms.ims.product.domain.valueobject.ImageUrl;
import io.thiiagoms.ims.product.domain.valueobject.Price;
import io.thiiagoms.ims.product.domain.valueobject.StockQuantity;
import io.thiiagoms.ims.product.domain.valueobject.Title;
import io.thiiagoms.ims.shared.domain.valueobject.Id;
import java.util.Optional;

public record UpdateProductData(
    Id id,
    Optional<Title> title,
    Optional<Description> description,
    Optional<ImageUrl> imageUrl,
    Optional<Price> price,
    Optional<StockQuantity> stockQuantity) {}
