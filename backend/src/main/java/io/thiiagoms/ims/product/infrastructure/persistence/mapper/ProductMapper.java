package io.thiiagoms.ims.product.infrastructure.persistence.mapper;

import io.thiiagoms.ims.category.infrastructure.persistence.model.CategoryJpa;
import io.thiiagoms.ims.product.domain.Product;
import io.thiiagoms.ims.product.domain.valueobject.CategoryIds;
import io.thiiagoms.ims.product.domain.valueobject.Description;
import io.thiiagoms.ims.product.domain.valueobject.ExpiryDate;
import io.thiiagoms.ims.product.domain.valueobject.ImageUrl;
import io.thiiagoms.ims.product.domain.valueobject.Price;
import io.thiiagoms.ims.product.domain.valueobject.Sku;
import io.thiiagoms.ims.product.domain.valueobject.StockQuantity;
import io.thiiagoms.ims.product.domain.valueobject.Title;
import io.thiiagoms.ims.product.infrastructure.persistence.model.ProductJpa;
import io.thiiagoms.ims.shared.domain.valueobject.Id;
import io.thiiagoms.ims.supplier.infrastructure.persistence.model.SupplierJpa;
import java.util.Set;
import java.util.UUID;

public final class ProductMapper {
  private ProductMapper() {}

  public static ProductJpa toPersistence(
      Product product, Set<CategoryJpa> categories, SupplierJpa supplier) {
    return ProductJpa.builder()
        .id(UUID.fromString(product.id().value()))
        .title(product.title().value())
        .description(product.description().value())
        .sku(product.sku().value())
        .imageUrl(product.imageUrl().value())
        .price(product.price().value())
        .stockQuantity(product.stockQuantity().value())
        .categories(Set.copyOf(categories))
        .supplier(supplier)
        .expiryDate(product.expiryDate().value())
        .build();
  }

  public static Product toDomain(ProductJpa product) {
    return Product.rehydrate(
        new Id(product.getId().toString()),
        new Title(product.getTitle()),
        new Description(product.getDescription()),
        new Sku(product.getSku()),
        new ImageUrl(product.getImageUrl()),
        new Price(product.getPrice()),
        new StockQuantity(product.getStockQuantity()),
        CategoryIds.rehydrate(
            product.getCategories().stream()
                .map(category -> new Id(category.getId().toString()))
                .sorted(java.util.Comparator.comparing(Id::value))
                .toList()),
        new Id(product.getSupplier().getId().toString()),
        ExpiryDate.rehydrate(product.getExpiryDate()));
  }
}
