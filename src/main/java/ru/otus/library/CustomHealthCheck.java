package ru.otus.library;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import ru.otus.library.model.entity.Book;
import ru.otus.library.repository.BookRepository;

import java.util.List;
import java.util.Objects;

/**
 * http://localhost:8080/actuator/health
 */
@Component
public class CustomHealthCheck implements HealthIndicator {

    private BookRepository bookRepository;

    public CustomHealthCheck(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public Health health() {
        final List<Book> books = bookRepository.findAll();
        final Long booksWithoutComments = books.stream().filter(book -> Objects.isNull(book.getComments()) ||
                book.getComments().size() == 0).count();
        if (booksWithoutComments >= 1L) {
            return Health.down().withDetail("Books without comments count: ", booksWithoutComments).build();
        }
        return Health.up().withDetail("Books without comments count: ", booksWithoutComments).build();
    }
}
