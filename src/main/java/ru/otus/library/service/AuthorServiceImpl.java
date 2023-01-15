package ru.otus.library.service;

import org.springframework.stereotype.Service;
import ru.otus.library.model.entity.Author;
import ru.otus.library.repository.AuthorRepository;

import java.util.List;

@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    public AuthorServiceImpl(final AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public Author findAuthorById(Long id) {
        return authorRepository.findById(id).orElseThrow();
    }

    @Override
    public Author findAuthorByName(String name) {
        return authorRepository.findAuthorByName(name);
    }

    @Override
    public List<String> findAllAuthorsNames() {
        return authorRepository.findAllAuthorsNames();
    }

    @Override
    public List<Author> findAllAuthors() {
        return authorRepository.findAll();
    }

    @Override
    public void deleteAuthor(final Author author) {
        authorRepository.delete(author);
    }

    @Override
    public void deleteAuthorById(final Long id) {
        authorRepository.deleteById(id);
    }

    @Override
    public void deleteAll() {
        authorRepository.deleteAll();
    }

    @Override
    public Author saveAuthor(final Author author) {
        return authorRepository.save(author);
    }
}
