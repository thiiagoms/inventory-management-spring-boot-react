package io.thiiagoms.ims.supplier.application.dto;

import io.thiiagoms.ims.shared.domain.pagination.Page;
import io.thiiagoms.ims.supplier.domain.Supplier;
import java.util.List;

public record SupplierPageOutput(
    List<SupplierOutput> content,
    int page,
    int size,
    long totalElements,
    int totalPages,
    boolean first,
    boolean last) {
  public SupplierPageOutput {
    content = List.copyOf(content);
  }

  public static SupplierPageOutput from(Page<Supplier> suppliers) {
    return new SupplierPageOutput(
        suppliers.content().stream().map(SupplierOutput::from).toList(),
        suppliers.page(),
        suppliers.size(),
        suppliers.totalElements(),
        suppliers.totalPages(),
        suppliers.first(),
        suppliers.last());
  }
}
