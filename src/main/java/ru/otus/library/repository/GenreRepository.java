package ru.otus.library.repository;

import ru.otus.library.model.entity.Genre;

import java.util.List;

public interface GenreRepository {

    Genre findGenreByName(final String genreName);

    List<Genre> findAllGenres();

    Genre saveGenre(final Genre genre);

    int deleteGenre(final Genre genre);

    int deleteAll();
}
