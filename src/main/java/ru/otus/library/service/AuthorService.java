package ru.otus.library.service;

import ru.otus.library.model.entity.Author;

import java.util.List;

public interface AuthorService {

    List<String> findAllAuthorsNames();

    boolean saveAuthor(final Author author);

    void deleteAuthor(final Author author);

    boolean deleteAuthorById(final Long id);

    void deleteAll();
}
