package ru.otus.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.library.model.entity.Genre;

public interface GenreRepository extends JpaRepository<Genre, Long> {

    Genre findByName(final String genreName);
}
