package io.thiiagoms.ims.user.infrastructure.persistence.repository;

import io.thiiagoms.ims.shared.domain.valueobject.Id;
import io.thiiagoms.ims.user.domain.User;
import io.thiiagoms.ims.user.domain.repository.UserRepository;
import io.thiiagoms.ims.user.domain.valueobject.Email;
import io.thiiagoms.ims.user.domain.valueobject.Phone;
import io.thiiagoms.ims.user.infrastructure.persistence.mapper.UserMapper;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepositoryImpl implements UserRepository {

  private final UserJpaRepository repository;

  public UserRepositoryImpl(UserJpaRepository repository) {
    this.repository = repository;
  }

  @Override
  public Optional<User> findById(Id id) {
    return repository.findById(UUID.fromString(id.value())).map(UserMapper::toDomain);
  }

  @Override
  public Optional<User> findByEmail(Email email) {
    return repository.findByEmail(email.value()).map(UserMapper::toDomain);
  }

  @Override
  public Optional<User> findByPhone(Phone phone) {
    return repository.findByPhone(phone.value()).map(UserMapper::toDomain);
  }

  @Override
  public void save(User user) {
    repository.save(UserMapper.toPersistence(user));
  }

  @Override
  public void destroy(Id id) {
    repository.deleteById(UUID.fromString(id.value()));
  }
}
