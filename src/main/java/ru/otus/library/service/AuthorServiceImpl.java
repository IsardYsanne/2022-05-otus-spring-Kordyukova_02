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
    public List<String> findAllAuthorsNames() {
        return authorRepository.findAllAuthorsNames();
    }

    @Override
    public void deleteAuthor(final Author author) {
        authorRepository.deleteAuthor(author);
    }

    @Override
    public boolean deleteAuthorById(final Long id) {
        final int result = authorRepository.deleteAuthorById(id);
        return result > 0;
    }

    @Override
    public void deleteAll() {
        authorRepository.deleteAll();
    }

    @Override
    public boolean saveAuthor(final Author author) {
        Author result = authorRepository.saveAuthor(author);
        return result.getId() != null;
    }
}
