package io.thiiagoms.ims.category.infrastructure.persistence.model;

import io.thiiagoms.ims.shared.infrastructure.persistence.model.BaseJpaEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@Entity
@Table(name = "categories")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CategoryJpa extends BaseJpaEntity {

  @Column(name = "title", nullable = false, unique = true, length = 250)
  private String title;

  @Column(name = "description", nullable = false, columnDefinition = "TEXT")
  private String description;
}
