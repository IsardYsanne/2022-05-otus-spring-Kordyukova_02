package ru.otus.library.service;

import ru.otus.library.model.entity.Author;

import java.util.List;

public interface AuthorService {

    Author findAuthorById(final Long id);

    Author findAuthorByName(final String name);

    List<String> findAllAuthorsNames();

    boolean saveAuthor(final Author author);

    void deleteAuthor(final Author author);

    void deleteAuthorById(final Long id);

    void deleteAll();
}
