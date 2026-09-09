package io.thiiagoms.ims.product.application.usecase.register;

import io.thiiagoms.ims.category.application.service.CategoryFinder;
import io.thiiagoms.ims.product.application.dto.ProductOutput;
import io.thiiagoms.ims.product.application.service.ProductUniqueness;
import io.thiiagoms.ims.product.domain.Product;
import io.thiiagoms.ims.product.domain.SkuGenerator;
import io.thiiagoms.ims.product.domain.repository.ProductRepository;
import io.thiiagoms.ims.product.domain.valueobject.Sku;
import io.thiiagoms.ims.shared.domain.identity.IdentityGenerator;
import io.thiiagoms.ims.supplier.application.service.SupplierFinder;
import org.springframework.transaction.annotation.Transactional;

public class RegisterProduct {

  private final CategoryFinder categoryFinder;

  private final ProductRepository repository;

  private final IdentityGenerator identityGenerator;

  private final SkuGenerator skuGenerator;

  private final ProductUniqueness uniqueness;

  private final SupplierFinder supplierFinder;

  public RegisterProduct(
      CategoryFinder categoryFinder,
      ProductRepository repository,
      IdentityGenerator identityGenerator,
      SkuGenerator skuGenerator,
      ProductUniqueness uniqueness,
      SupplierFinder supplierFinder) {
    this.uniqueness = uniqueness;
    this.repository = repository;
    this.categoryFinder = categoryFinder;
    this.identityGenerator = identityGenerator;
    this.skuGenerator = skuGenerator;
    this.supplierFinder = supplierFinder;
  }

  @Transactional
  public ProductOutput execute(RegisterProductData data) {
    uniqueness.ensureTitleIsAvailable(data.title());
    Sku sku = skuGenerator.generate(data.title());
    uniqueness.ensureSkuIsAvailable(sku);
    data.categoryIds().values().forEach(categoryFinder::byId);
    supplierFinder.byId(data.supplierId());
    Product product = build(data, sku);

    repository.save(product);

    return ProductOutput.from(product);
  }

  private Product build(RegisterProductData data, Sku sku) {
    return Product.register(
        identityGenerator.generate(),
        data.title(),
        data.description(),
        sku,
        data.imageUrl(),
        data.price(),
        data.stockQuantity(),
        data.categoryIds(),
        data.supplierId(),
        data.expiryDate());
  }
}
