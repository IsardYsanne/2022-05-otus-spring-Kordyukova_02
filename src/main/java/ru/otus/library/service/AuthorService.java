package ru.otus.library.service;

import ru.otus.library.model.entity.Author;

import java.util.List;

public interface AuthorService {

    List<String> findAllAuthorsNames();

    Author saveAuthor(final Author author);

    void updateAuthorById(final String authorId, final String newAuthorName, final String bookId);

    void deleteAll();
}
