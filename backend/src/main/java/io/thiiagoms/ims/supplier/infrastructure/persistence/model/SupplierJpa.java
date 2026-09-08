package io.thiiagoms.ims.supplier.infrastructure.persistence.model;

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
@Table(name = "suppliers")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SupplierJpa extends BaseJpaEntity {
  @Column(name = "social_name", nullable = false, length = 250)
  private String socialName;

  @Column(name = "cnpj", nullable = false, unique = true, length = 14)
  private String cnpj;

  @Column(name = "address", nullable = false, length = 500)
  private String address;
}
