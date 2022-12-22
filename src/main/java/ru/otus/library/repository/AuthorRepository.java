package ru.otus.library.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.library.model.entity.Author;

public interface AuthorRepository extends MongoRepository<Author, String> {

    Author findAuthorById(String id);
}
