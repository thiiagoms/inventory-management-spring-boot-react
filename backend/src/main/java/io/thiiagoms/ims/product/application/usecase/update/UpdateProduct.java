package io.thiiagoms.ims.product.application.usecase.update;

import io.thiiagoms.ims.product.application.dto.ProductOutput;
import io.thiiagoms.ims.product.application.exception.ProductNotChangedException;
import io.thiiagoms.ims.product.application.service.ProductFinder;
import io.thiiagoms.ims.product.application.service.ProductUniqueness;
import io.thiiagoms.ims.product.domain.Product;
import io.thiiagoms.ims.product.domain.repository.ProductRepository;
import org.springframework.transaction.annotation.Transactional;

public class UpdateProduct {

  private final ProductFinder finder;

  private final ProductRepository repository;

  private final ProductUniqueness uniqueness;

  public UpdateProduct(
      ProductFinder finder, ProductRepository repository, ProductUniqueness uniqueness) {
    this.finder = finder;
    this.repository = repository;
    this.uniqueness = uniqueness;
  }

  @Transactional
  public ProductOutput execute(UpdateProductData data) {
    var product = finder.byId(data.id());
    boolean changed = update(product, data);

    if (!changed) {
      throw ProductNotChangedException.create();
    }

    repository.save(product);
    return ProductOutput.from(product);
  }

  private boolean update(Product product, UpdateProductData data) {
    boolean changed = updateTitle(product, data);
    changed |= updateDescription(product, data);
    changed |= updateImageUrl(product, data);
    changed |= updatePrice(product, data);
    changed |= updateStockQuantity(product, data);
    return changed;
  }

  private boolean updateTitle(Product product, UpdateProductData data) {
    return data.title()
        .filter(title -> !title.equals(product.title()))
        .map(
            title -> {
              uniqueness.ensureTitleIsAvailable(title);
              product.changeTitleTo(title);
              return true;
            })
        .orElse(false);
  }

  private boolean updateDescription(Product product, UpdateProductData data) {
    return data.description()
        .filter(description -> !description.equals(product.description()))
        .map(description -> change(() -> product.changeDescriptionTo(description)))
        .orElse(false);
  }

  private boolean updateImageUrl(Product product, UpdateProductData data) {
    return data.imageUrl()
        .filter(imageUrl -> !imageUrl.equals(product.imageUrl()))
        .map(imageUrl -> change(() -> product.changeImageUrlTo(imageUrl)))
        .orElse(false);
  }

  private boolean updatePrice(Product product, UpdateProductData data) {
    return data.price()
        .filter(price -> !price.equals(product.price()))
        .map(price -> change(() -> product.changePriceTo(price)))
        .orElse(false);
  }

  private boolean updateStockQuantity(Product product, UpdateProductData data) {
    return data.stockQuantity()
        .filter(stockQuantity -> !stockQuantity.equals(product.stockQuantity()))
        .map(stockQuantity -> change(() -> product.changeStockQuantityTo(stockQuantity)))
        .orElse(false);
  }

  private boolean change(Runnable mutation) {
    mutation.run();
    return true;
  }
}
