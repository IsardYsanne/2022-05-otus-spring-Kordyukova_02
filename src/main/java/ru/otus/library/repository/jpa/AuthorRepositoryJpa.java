package ru.otus.library.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.otus.library.model.entity.jpa.AuthorJpa;

import java.util.List;

public interface AuthorRepositoryJpa extends JpaRepository<AuthorJpa, Long> {

    AuthorJpa findAuthorByName(final String name);

    @Query("SELECT a.name FROM AuthorJpa a")
    List<String> findAllAuthorsNames();
}