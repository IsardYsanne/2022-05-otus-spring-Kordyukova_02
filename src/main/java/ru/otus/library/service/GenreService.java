package ru.otus.library.service;

import ru.otus.library.model.entity.jpa.GenreJpa;

import java.util.List;

public interface GenreService {

    GenreJpa findByName(String name);

    List<String> findAllGenres();

    boolean saveNewGenre(GenreJpa genre);

    void deleteGenre(final String genreName);

    void deleteAll();
}
