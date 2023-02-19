package ru.otus.library.repository;

import org.springframework.data.repository.CrudRepository;
import ru.otus.library.model.entity.User;

public interface UserRepository extends CrudRepository<User, Long> {

    User findUserByUserName(String name);
}
