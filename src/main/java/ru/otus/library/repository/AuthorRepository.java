package ru.otus.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.otus.library.model.entity.Author;

import java.util.List;

public interface AuthorRepository extends JpaRepository<Author, Long> {

    Author findAuthorByName(final String name);

    @Query("SELECT a.name FROM Author a")
    List<String> findAllAuthorsNames();
}
