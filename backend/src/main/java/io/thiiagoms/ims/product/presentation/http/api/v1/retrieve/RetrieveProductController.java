package io.thiiagoms.ims.product.presentation.http.api.v1.retrieve;

import io.thiiagoms.ims.product.application.usecase.retrieve.RetrieveProduct;
import io.thiiagoms.ims.product.application.usecase.retrieve.RetrieveProducts;
import io.thiiagoms.ims.product.presentation.http.api.v1.ProductController;
import io.thiiagoms.ims.product.presentation.http.api.v1.shared.ProductPageResponse;
import io.thiiagoms.ims.product.presentation.http.api.v1.shared.ProductResponse;
import io.thiiagoms.ims.shared.domain.pagination.Pagination;
import io.thiiagoms.ims.shared.domain.valueobject.Id;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RetrieveProductController implements ProductController {
  private final RetrieveProduct retrieveProduct;
  private final RetrieveProducts retrieveProducts;

  RetrieveProductController(RetrieveProduct retrieveProduct, RetrieveProducts retrieveProducts) {
    this.retrieveProduct = retrieveProduct;
    this.retrieveProducts = retrieveProducts;
  }

  @GetMapping("/{id}")
  public ProductResponse show(@PathVariable String id) {
    return ProductResponse.from(retrieveProduct.execute(new Id(id)));
  }

  @GetMapping
  public ProductPageResponse index(
      @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
    return ProductPageResponse.from(retrieveProducts.execute(new Pagination(page, size)));
  }
}
