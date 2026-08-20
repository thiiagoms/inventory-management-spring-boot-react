package io.thiiagoms.ims.fixtures.user.infrastructure.persistence.repository;

import io.thiiagoms.ims.shared.domain.valueobject.Id;
import io.thiiagoms.ims.user.domain.User;
import io.thiiagoms.ims.user.domain.repository.UserRepository;
import io.thiiagoms.ims.user.domain.valueobject.Email;
import io.thiiagoms.ims.user.domain.valueobject.Phone;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserMemoryRepository implements UserRepository {

  private List<User> users = new ArrayList<>();

  @Override
  public Optional<User> findById(Id id) {
    return users.stream().filter(user -> user.id().equals(id)).findFirst();
  }

  @Override
  public Optional<User> findByEmail(Email email) {
    return users.stream().filter(user -> user.email().equals(email)).findFirst();
  }

  @Override
  public Optional<User> findByPhone(Phone phone) {
    return users.stream().filter(user -> user.phone().equals(phone)).findFirst();
  }

  @Override
  public void save(User user) {
    users.add(user);
  }

  @Override
  public void destroy(Id id) {
    users.removeIf(searchableUser -> searchableUser.id().equals(id));
  }
}
