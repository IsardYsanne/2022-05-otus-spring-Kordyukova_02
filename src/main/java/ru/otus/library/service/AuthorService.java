package ru.otus.library.service;

import ru.otus.library.model.entity.jpa.AuthorJpa;

import java.util.List;

public interface AuthorService {

    AuthorJpa findAuthorById(final Long id);

    AuthorJpa findAuthorByName(final String name);

    List<String> findAllAuthorsNames();

    boolean saveAuthor(final AuthorJpa author);

    void deleteAuthor(final AuthorJpa author);

    void deleteAuthorById(final Long id);

    void deleteAll();
}
