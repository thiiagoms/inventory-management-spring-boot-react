package io.thiiagoms.ims.user.application.usecase.destroy;

import io.thiiagoms.ims.shared.domain.valueobject.Id;
import io.thiiagoms.ims.user.application.service.UserFinder;
import io.thiiagoms.ims.user.domain.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;

public class DestroyUser {

  private final UserFinder finder;

  private final UserRepository repository;

  public DestroyUser(UserFinder finder, UserRepository repository) {
    this.finder = finder;
    this.repository = repository;
  }

  @Transactional
  public void execute(Id id) {
    finder.byId(id);
    repository.destroy(id);
  }
}
