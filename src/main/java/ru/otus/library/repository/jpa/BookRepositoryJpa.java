package ru.otus.library.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.otus.library.model.entity.jpa.BookJpa;

import java.util.List;

public interface BookRepositoryJpa extends JpaRepository<BookJpa, Long> {

    @Query("SELECT b FROM BookJpa b JOIN b.authors a WHERE a.id = :id")
    List<BookJpa> findBooksByAuthorId(@Param(value = "id") Long authorId);

    List<BookJpa> findBooksByTitle(final String title);

    @Query("SELECT b.title FROM BookJpa b")
    List<String> findAllTitles();
}
