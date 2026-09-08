package io.thiiagoms.ims.supplier.presentation.http.api.v1;

import io.thiiagoms.ims.supplier.application.dto.SupplierPageOutput;
import java.util.List;

public record SupplierPageResponse(
    List<SupplierResponse> content,
    int page,
    int size,
    long totalElements,
    int totalPages,
    boolean first,
    boolean last) {
  public SupplierPageResponse {
    content = List.copyOf(content);
  }

  public static SupplierPageResponse from(SupplierPageOutput output) {
    return new SupplierPageResponse(
        output.content().stream().map(SupplierResponse::from).toList(),
        output.page(),
        output.size(),
        output.totalElements(),
        output.totalPages(),
        output.first(),
        output.last());
  }
}
