package ru.otus.library.repository;

import org.springframework.data.repository.CrudRepository;
import ru.otus.library.model.entity.Genre;

import java.util.List;

public interface GenreRepository extends CrudRepository<Genre, Long> {

    Genre findByName(final String genreName);

    List<Genre> findAll();
}
