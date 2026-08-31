package io.thiiagoms.ims.shared.domain.pagination;

import java.util.List;

public record Page<T>(
    List<T> content,
    int page,
    int size,
    long totalElements,
    int totalPages,
    boolean first,
    boolean last) {
  public Page {
    content = List.copyOf(content);
  }
}
