package io.thiiagoms.ims.supplier.presentation.http.api.v1;

import io.thiiagoms.ims.shared.domain.pagination.Pagination;
import io.thiiagoms.ims.shared.domain.valueobject.Id;
import io.thiiagoms.ims.supplier.application.usecase.destroy.DestroySupplier;
import io.thiiagoms.ims.supplier.application.usecase.register.RegisterSupplier;
import io.thiiagoms.ims.supplier.application.usecase.retrieve.RetrieveSupplier;
import io.thiiagoms.ims.supplier.application.usecase.retrieve.RetrieveSuppliers;
import io.thiiagoms.ims.supplier.application.usecase.update.UpdateSupplier;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/suppliers")
public class SupplierController {
  private final RegisterSupplier registerSupplier;
  private final RetrieveSupplier retrieveSupplier;
  private final RetrieveSuppliers retrieveSuppliers;
  private final UpdateSupplier updateSupplier;
  private final DestroySupplier destroySupplier;

  SupplierController(
      RegisterSupplier registerSupplier,
      RetrieveSupplier retrieveSupplier,
      RetrieveSuppliers retrieveSuppliers,
      UpdateSupplier updateSupplier,
      DestroySupplier destroySupplier) {
    this.registerSupplier = registerSupplier;
    this.retrieveSupplier = retrieveSupplier;
    this.retrieveSuppliers = retrieveSuppliers;
    this.updateSupplier = updateSupplier;
    this.destroySupplier = destroySupplier;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public SupplierResponse store(@Valid @RequestBody RegisterSupplierRequest request) {
    return SupplierResponse.from(registerSupplier.execute(request.toCommand()));
  }

  @GetMapping("/{id}")
  public SupplierResponse show(@PathVariable String id) {
    return SupplierResponse.from(retrieveSupplier.execute(new Id(id)));
  }

  @GetMapping
  public SupplierPageResponse index(
      @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
    return SupplierPageResponse.from(retrieveSuppliers.execute(new Pagination(page, size)));
  }

  @PatchMapping("/{id}")
  public SupplierResponse update(
      @PathVariable String id, @RequestBody UpdateSupplierRequest request) {
    return SupplierResponse.from(updateSupplier.execute(request.toCommand(id)));
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void destroy(@PathVariable String id) {
    destroySupplier.execute(new Id(id));
  }
}
