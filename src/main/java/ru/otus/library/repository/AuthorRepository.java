package ru.otus.library.repository;

import ru.otus.library.model.entity.Author;

import java.util.List;

public interface AuthorRepository {

    Author findAuthorById(final String id);

    Author findAuthorByName(final String name);

    List<String> findAllAuthorsNames();

    Author saveAuthor(final Author author);

    int deleteAuthor(final Author author);

    int deleteAuthorById(final Long id);

    int deleteAll();
}
