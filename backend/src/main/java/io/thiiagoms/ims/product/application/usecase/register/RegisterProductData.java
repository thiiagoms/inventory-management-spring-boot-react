package io.thiiagoms.ims.product.application.usecase.register;

import io.thiiagoms.ims.product.domain.valueobject.CategoryIds;
import io.thiiagoms.ims.product.domain.valueobject.Description;
import io.thiiagoms.ims.product.domain.valueobject.ExpiryDate;
import io.thiiagoms.ims.product.domain.valueobject.ImageUrl;
import io.thiiagoms.ims.product.domain.valueobject.Price;
import io.thiiagoms.ims.product.domain.valueobject.StockQuantity;
import io.thiiagoms.ims.product.domain.valueobject.Title;

public record RegisterProductData(
    Title title,
    Description description,
    ImageUrl imageUrl,
    Price price,
    StockQuantity stockQuantity,
    CategoryIds categoryIds,
    ExpiryDate expiryDate) {}
