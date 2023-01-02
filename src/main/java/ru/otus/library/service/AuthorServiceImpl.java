package ru.otus.library.service;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import ru.otus.library.model.entity.Author;
import ru.otus.library.model.entity.Book;
import ru.otus.library.repository.AuthorRepository;
import ru.otus.library.repository.BookRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class AuthorServiceImpl implements AuthorService {

    private AuthorRepository authorRepository;

    private BookRepository bookRepository;

    private MongoTemplate mongoTemplate;

    public AuthorServiceImpl(AuthorRepository authorRepository,
                             BookRepository bookRepository,
                             MongoTemplate mongoTemplate) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<String> findAllAuthorsNames() {
        final List<String> result = new ArrayList<>();
        authorRepository.findAll().forEach(author -> result.add(author.getName()));
        return result;
    }

    @Override
    public Author saveAuthor(Author author) {
        return authorRepository.save(author);
    }

    @Override
    @Transactional
    public void updateAuthorById(final String authorId, final String newAuthorName, String bookId) {
        Author author = authorRepository.findAuthorById(authorId);
        if (author != null) {
            Query query = new Query();
            query.addCriteria(Criteria.where("name").is(author.getName()));
            Update update = new Update();
            update.set("name", newAuthorName);
            mongoTemplate.updateFirst(query, update, Author.class);

            Book book = bookRepository.findById(bookId).orElseThrow();
            Set<Author> bookAuthors = new HashSet<>();
            bookAuthors.add(author);
            bookAuthors.stream().findFirst().orElseThrow().setName(newAuthorName);
            book.setAuthors(bookAuthors);
            bookRepository.save(book);
        }
    }

    @Override
    public void deleteAll() {
        bookRepository.findAll().forEach(book -> book.setAuthors(new HashSet<>()));
    }
}
