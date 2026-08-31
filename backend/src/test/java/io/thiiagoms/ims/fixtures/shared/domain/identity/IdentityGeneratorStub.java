package io.thiiagoms.ims.fixtures.shared.domain.identity;

import io.thiiagoms.ims.shared.domain.identity.IdentityGenerator;
import io.thiiagoms.ims.shared.domain.valueobject.Id;

public final class IdentityGeneratorStub implements IdentityGenerator {
  private Id nextId;
  private int numberOfCalls;

  public IdentityGeneratorStub willGenerate(Id id) {
    nextId = id;
    return this;
  }

  public int numberOfCalls() {
    return numberOfCalls;
  }

  @Override
  public Id generate() {
    numberOfCalls++;
    return nextId;
  }
}
