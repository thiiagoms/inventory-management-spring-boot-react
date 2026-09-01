package io.thiiagoms.ims.product.domain.valueobject;

import io.thiiagoms.ims.shared.domain.exception.InvalidDomainArgumentException;
import io.thiiagoms.ims.shared.domain.support.Guard;
import io.thiiagoms.ims.shared.domain.valueobject.Id;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

public final class CategoryIds {
  public static final String FIELD = "categoryIds";
  public static final int MINIMUM_SIZE = 3;

  private final List<Id> values;

  public CategoryIds(List<Id> values) {
    this(values, true);
  }

  private CategoryIds(List<Id> values, boolean enforceMinimum) {
    Guard.againstNull(FIELD, values);
    if (values.stream().anyMatch(Objects::isNull)) {
      throw fail("Category IDs cannot contain null values.");
    }
    if (enforceMinimum && values.size() < MINIMUM_SIZE) {
      throw fail("A product must have at least %d categories.".formatted(MINIMUM_SIZE));
    }
    if (new HashSet<>(values).size() != values.size()) {
      throw fail("A product cannot contain duplicate categories.");
    }
    this.values = List.copyOf(values);
  }

  public static CategoryIds rehydrate(List<Id> values) {
    return new CategoryIds(values, false);
  }

  public List<Id> values() {
    return values;
  }

  @Override
  public boolean equals(Object object) {
    return object instanceof CategoryIds other && values.equals(other.values);
  }

  @Override
  public int hashCode() {
    return values.hashCode();
  }

  private static InvalidDomainArgumentException fail(String message) {
    return InvalidDomainArgumentException.with(message, FIELD);
  }
}
