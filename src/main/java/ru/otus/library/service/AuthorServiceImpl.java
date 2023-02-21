package ru.otus.library.service;

import org.springframework.stereotype.Service;
import ru.otus.library.model.entity.jpa.AuthorJpa;
import ru.otus.library.repository.jpa.AuthorRepositoryJpa;
import ru.otus.library.repository.jpa.BookRepositoryJpa;

import java.util.List;

@Service
public class AuthorServiceImpl implements AuthorService {

    private AuthorRepositoryJpa authorRepository;

    private BookRepositoryJpa bookRepository;

    public AuthorServiceImpl(AuthorRepositoryJpa authorRepository,
                             BookRepositoryJpa bookRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public AuthorJpa findAuthorById(Long id) {
        return authorRepository.findById(id).orElseThrow();
    }

    @Override
    public AuthorJpa findAuthorByName(String name) {
        return authorRepository.findAuthorByName(name);
    }

    @Override
    public List<String> findAllAuthorsNames() {
        return authorRepository.findAllAuthorsNames();
    }

    @Override
    public void deleteAuthor(final AuthorJpa author) {
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
    public boolean saveAuthor(final AuthorJpa author) {
        AuthorJpa result = authorRepository.save(author);
        return result.getId() != null;
    }
}
