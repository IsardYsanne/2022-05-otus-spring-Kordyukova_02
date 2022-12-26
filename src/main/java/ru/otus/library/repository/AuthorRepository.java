package ru.otus.library.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.otus.library.model.entity.Author;

import java.util.List;

public interface AuthorRepository extends CrudRepository<Author, Long> {

    Author findAuthorByName(final String name);

    List<Author> findAll();

    @Query("SELECT a.name FROM Author a")
    List<String> findAllAuthorsNames();
}
